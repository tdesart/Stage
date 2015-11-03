package lu.uni.fstc.test_Remus;

import java.util.ArrayList;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import lu.uni.fstc.proactivity.pdf.GroupPdfBuilder;
import lu.uni.fstc.proactivity.pdf.PdfBuilder;


/**
 * @author REMUS
 *
 */
public class TestPdfBuilder {
	
	private final String groupName = "Luxembourg";
	private DefaultPieDataset dataSetCourseGroups;
	private DefaultCategoryDataset dataSetCourseYesNoStudents, groupNewLeaveStudents;
	@SuppressWarnings("unused")
	private PdfBuilder pdfBuilder;
	private XYSeriesCollection groupActivity;
//	private final String statsFileName = "courseStats_02";
	private int groupNumber;
	private final int week = 4;
	private double activityLevel;
//	private final PdfBuilder coursePdfBuilder; 
	private ArrayList<PdfBuilder> groupPdfBuilder = new ArrayList<PdfBuilder>();
	
	/**
	 * 
	 */
	public TestPdfBuilder(){
		// we get the info to their corresponding dataSets
		initializeDataSets1();
		initializeDataSets2();
		
		// then we build the Pdf
//		coursePdfBuilder = new CoursePdfBuilder(dataSetCourseGroups, dataSetCourseYesNoStudents);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final TestPdfBuilder a = new TestPdfBuilder();		
		a.groupPdfBuilder.add(new GroupPdfBuilder(a.groupActivity, a.groupNewLeaveStudents, a.groupName));
	}
	
	/**
	 * Initialises the dataSets according to the data in the database
	 */
	private void initializeDataSets1() {		
		/**
		 * New data is written to the pieChart each time a pieChart is created
		 * 	-- contains the current number of groups in the Course 'cops'
		 */
		
		groupNumber = 1;
		
		dataSetCourseGroups = new DefaultPieDataset();
		
		dataSetCourseGroups.setValue("Study program-based groups = " + groupNumber, groupNumber++);
		dataSetCourseGroups.setValue("Country-based groups = " + groupNumber, groupNumber++);
		dataSetCourseGroups.setValue("City-based groups = " + groupNumber, groupNumber);

		/**
		 * New data is added to the DualAxisChart each time the DualAxisChart  is created
		 * 	-- it adds the new numbers of 'YES'/'NO' answers that were registered during a week
		 * 	-- after a month (4 weeks new data is generated) ??
		 */
		
		dataSetCourseYesNoStudents = new DefaultCategoryDataset();
		
		final String series1 = "Ok";
        final String series2 = "No, thanks!";        
        final String [] categories = new String [week];
		
		for (int i = 0; i < week ; i++){
			categories [i] = (i+1)+"";
			dataSetCourseYesNoStudents.setValue(i, series1, categories[i]);
			dataSetCourseYesNoStudents.setValue(i+1, series2, categories[i]);
		}
	}
	
	/**
	 * Initialises the dataSets according to the data in the database
	 */
	private void initializeDataSets2() {
		
		/**
		 * New data is written to the xyChart each time a xyChart is created
		 * 	-- contains the current number of groups in the Course 'cops'
		 */
		groupActivity = new XYSeriesCollection();
		
		/**
		 * New data is added to the Timeline each time the Timeline is created
		 * 	-- it adds the new numbers of 'YES'/'NO' answers that were registered during a week
		 * 	-- after a month (4 weeks new data is generated) ??
		 */		
		groupNewLeaveStudents = new DefaultCategoryDataset();
		
		
		final String series1 = "New Students";
        final String series2 = "Students that left the group";
        final XYSeries series3 = new XYSeries("Activity");
        final String [] categories = new String [week];
		
		for (int i = 0; i < week ; i++){
			categories [i] = (i+1)+"";
					
			activityLevel = i + 1;
			series3.add(i+1, activityLevel);
			
			groupNewLeaveStudents.setValue(i, series1, categories[i]);
			groupNewLeaveStudents.setValue(i+1, series2, categories[i]);
		}
		
		groupActivity.addSeries(series3);
		groupActivity.addSeries(series3);
		
		
	}
	
}
