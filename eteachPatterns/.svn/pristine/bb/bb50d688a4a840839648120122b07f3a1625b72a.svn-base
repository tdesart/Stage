package lu.uni.fstc.proactivity.eteach.events;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * User Events are used to identify the events on the database that represents user interactions with the ETeach Interface<br> 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public enum UserEvent {

	/**
	 * (click on) Element of List
	 */
	EOL,

	/**
	 * (Scroll bar) click on arrow
	 */
	AR,

	/**
	 * (Scroll bar) click on SLIDER
	 */
	SR,

	/**
	 * Web Link
	 */
	WL,

	/**
	 * Mouse Hover Comment Box 
	 */
	MHC,

	/**
	 * Mouse Hover Result List 
	 */
	MHR,

	/**
	 * SEARCH BUTTON 1 
	 */
	SB1,

	/**
	 * SEARCH BUTTON 2 
	 */
	SB2,

	/**
	 * events that should be ignored by every pattern
	 * IGNORE 
	 */
	IGNORE,

	/**
	 * events that stop (reset) every pattern
	 * STOP 
	 */
	STOP;

	/**
	 * @param type
	 * @param element
	 * @param value
	 * @return the detected UserEvent based on 2 fields of the Record set
	 */
	static public UserEvent detectEvent (final String type, final String element, String value) {
		if (type.equalsIgnoreCase("list") && element.equalsIgnoreCase("result_list")) {
			if (value.equalsIgnoreCase("scroll_arrow"))
				return AR;
			else if (value.equalsIgnoreCase("scroll_slider"))
				return SR;
			else if (value.equalsIgnoreCase("hovering stop"))
				return MHR;
			else if (value.equalsIgnoreCase("hovering start"))
				return IGNORE;
			else // if every thing else fails, it means it's a click on an element of the list
				return EOL;
		}
		else if (type.equalsIgnoreCase("comment") && element.equalsIgnoreCase("comment")) {
			if (value.equalsIgnoreCase("hovering stop"))
				return MHC;
			else if (value.equalsIgnoreCase("hovering start"))
				return IGNORE;
		}
		else if (type.equalsIgnoreCase("link") && element.equalsIgnoreCase("click")) 
			return WL;
		else if (type.equalsIgnoreCase("page") && element.equalsIgnoreCase("search")) 
			return SB1;
		else if (type.equalsIgnoreCase("page") && element.equalsIgnoreCase("result")) 
			return SB2;

		Global.logger.fine("Event type = "+ type + ", element = " + element + ", value = " + value);
		// every other case should return s signal indicating that the patterns are not met!
		return STOP;
	}
}