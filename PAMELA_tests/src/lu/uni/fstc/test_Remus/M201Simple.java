package lu.uni.fstc.test_Remus;

import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.rules.moodleCoPs.AbstractRuleCops;
import lu.uni.fstc.proactivity.rules.moodleCoPs.ModInfo;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author remus.dobrican
 * 
 */
public class M201Simple extends AbstractRuleCops {

	private long startTime, currentTime, course_ID, pdf_context_ID,
			pdf_resource_ID, course_modules_ID, course_section_ID, grouping_ID;
	private String path, groupName;
	private PdfBuilderSimple pdfBuilder;
	private final String statsFileName = "courseStats_02";

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private M201Simple() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param course_ID
	 * @param groupName 
	 * @param startTime
	 */
	public M201Simple(final long course_ID, final String groupName, final long startTime) {
		this();
		this.course_ID = course_ID;
		this.groupName = groupName;
		this.startTime = startTime;
	}

	@Override
	protected void dataAcquisition() {
		currentTime = System.currentTimeMillis();
	}

	@Override
	protected boolean activationGuards() {
		if (currentTime > startTime)
			return true;
		return false;
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	/*
	 * creates resources for a certain group
	 */
	@Override
	public void actions() {
		Global.logger.finer(this.toString());
		
		// then we build the Pdf
		pdfBuilder = new PdfBuilderSimple(groupName, groupName+1);

		creatingThePdf();

	}

	/**
	 * Doesn't generate other rules
	 */
	@Override
	protected boolean rulesGeneration() {
		return true;
	}

	@Override
	public String toString() {
		return "M201 - Creating statistics for the users";
	}

	/**
	 * Builds and registers the pdf to Moodle
	 */
	private void creatingThePdf() {
		
		// inserting "."
		dataNativeSystem.insertIntoFiles("draft", pdfBuilder.getContenthashString(),
				pdfBuilder.getPathnamehashString(), pdf_context_ID, ".",
				pdfBuilder.getStringSize());
		// inserting "Statistics.pdf"
		dataNativeSystem.insertIntoFiles("draftEmpty", pdfBuilder.getContenthashFile(),
				pdfBuilder.getPathnamehashFile(), pdf_context_ID,
				pdfBuilder.getFileName() + ".pdf", pdfBuilder.getFileSize());
		
		grouping_ID = dataNativeSystem.getIDFromGroupingName(groupName);
		dataNativeSystem.insertIntoCourseModules(13, course_ID, grouping_ID);
		course_modules_ID = dataNativeSystem.getLastId(); // gets the last inserted course_modules_id

		dataNativeSystem.insertIntoResources(pdfBuilder.getFileName(), course_ID, pdfBuilder.getPdfDescription(), pdfBuilder.getDisplayOptions());
		pdf_resource_ID = dataNativeSystem.getLastId(); // gets the last inserted resource_id
		
		// context_id
		dataNativeSystem.insertIntoContextTable("pdf", course_modules_ID);
		pdf_context_ID = dataNativeSystem.getContextID(course_modules_ID, 4);
		path = dataNativeSystem.getContextPath(course_ID, 3) + "/" + pdf_context_ID;
		dataNativeSystem.updateContext(path, pdf_context_ID);
		
		
		pdfBuilder.buildHashs("/" + pdf_context_ID
				+ "/mod_resource/content/0/.", "/" + pdf_context_ID
				+ "/mod_resource/content/0/"+ statsFileName +".pdf");

		
		// inserting "."
		dataNativeSystem.insertIntoFiles("empty", pdfBuilder.getContenthashString(),
				pdfBuilder.getPathnamehashString(), pdf_context_ID, ".",
				pdfBuilder.getStringSize());
		// inserting "Statistics.pdf"
		dataNativeSystem.insertIntoFiles("pdf", pdfBuilder.getContenthashFile(),
				pdfBuilder.getPathnamehashFile(), pdf_context_ID,
				pdfBuilder.getFileName() + ".pdf", pdfBuilder.getFileSize());

		course_section_ID = dataNativeSystem.getCourseSectionID(course_ID, 0);
		dataNativeSystem.updateCourseModule(pdf_resource_ID, course_section_ID, course_modules_ID);
		dataNativeSystem.updateCourseSection("," + course_modules_ID, course_section_ID);

		String modinfo = dataNativeSystem.getCourseModinfo(course_ID);
		ModInfo pdfModInfo = new ModInfo(modinfo, String.valueOf(pdf_resource_ID), 14, String.valueOf(course_modules_ID), "resource", String.valueOf(0), String.valueOf(grouping_ID), pdfBuilder.getFileName());
		dataNativeSystem.updateCourseModinfo(pdfModInfo.getModinfo(), course_ID);
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the course_ID
	 */
	public long getCourse_ID() {
		return course_ID;
	}

	/**
	 * @param course_ID
	 *            the course_ID to set
	 */
	public void setCourse_ID(long course_ID) {
		this.course_ID = course_ID;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
