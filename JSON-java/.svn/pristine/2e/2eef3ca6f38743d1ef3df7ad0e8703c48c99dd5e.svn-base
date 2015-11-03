package lu.uni.fstc.converter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * <b>Converting from ResultSet to JSONArray</b> - Original version from the answer on Nov 19 2012 by Li3ro on <code><a href="http://stackoverflow.com/questions/6514876/most-effecient-conversion-of-resultset-to-json"> stackoverflow.com </a> <br></code><p> 
 * Sandro Reis updates: <br>
 * <li>created the package <br>
 * <li>added the imports <br>
 * <li>replaced column_name for i, in all <code>gets</code> for performance purposes<br>
 * <p>
 * @author Sandro Reis - updates
 * @version 1.0 - Sandro Reis © 2013
 * 
 */
public class ResultSetConverter {
	/**
	 * Original version from the answer on Nov 19 2012 by Li3ro on <code><a href="http://stackoverflow.com/questions/6514876/most-effecient-conversion-of-resultset-to-json"> stackoverflow.com </a> <br></code><p> 
	 * Changed : <br>
	 * <li>replaced <b>getColumnName</b> for <b>getColumnLabel</b> to get the column's suggested title, not necessarily the column name<br>
	 * <li>replaced <b>column_name</b> for <b>i</b>, in all <code>gets</code> for performance purposes<br>
	 * <p>
	 * 
	 * @see <a href="http://stackoverflow.com/questions/6514876/most-effecient-conversion-of-resultset-to-json">http://stackoverflow.com/questions/6514876/most-effecient-conversion-of-resultset-to-json</a>
	 * @author Li3ro - Original
	 * @param rs
	 * @return the JSONArray that represents the complete ResultSet in JSON format
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static JSONArray toJSONArray(final ResultSet rs) throws SQLException, JSONException {
		JSONArray json = new JSONArray();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();

		while (rs.next()) {
			JSONObject obj = new JSONObject();
			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnLabel(i);
				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(column_name, rs.getArray(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(column_name, rs.getLong(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.REAL) {
					obj.put(column_name, rs.getFloat(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(column_name, rs.getBoolean(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(column_name, rs.getBlob(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(column_name, rs.getDouble(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(column_name, rs.getDouble(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(column_name, rs.getInt(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(column_name, rs.getNString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(column_name, rs.getString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.CHAR) {
					obj.put(column_name, rs.getString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NCHAR) {
					obj.put(column_name, rs.getNString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGNVARCHAR) {
					obj.put(column_name, rs.getNString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARCHAR) {
					obj.put(column_name, rs.getString(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(column_name, rs.getByte(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(column_name, rs.getShort(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(column_name, rs.getDate(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIME) {
					obj.put(column_name, rs.getTime(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(column_name, rs.getTimestamp(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BINARY) {
					obj.put(column_name, rs.getBytes(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARBINARY) {
					obj.put(column_name, rs.getBytes(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARBINARY) {
					obj.put(column_name, rs.getBinaryStream(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIT) {
					obj.put(column_name, rs.getBoolean(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.CLOB) {
					obj.put(column_name, rs.getClob(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
					obj.put(column_name, rs.getBigDecimal(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DECIMAL) {
					obj.put(column_name, rs.getBigDecimal(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATALINK) {
					obj.put(column_name, rs.getURL(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.REF) {
					obj.put(column_name, rs.getRef(i));
				} else if (rsmd.getColumnType(i) == java.sql.Types.STRUCT) {
					obj.put(column_name, rs.getObject(i)); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
				} else if (rsmd.getColumnType(i) == java.sql.Types.DISTINCT) {
					obj.put(column_name, rs.getObject(i)); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
				} else if (rsmd.getColumnType(i) == java.sql.Types.JAVA_OBJECT) {
					obj.put(column_name, rs.getObject(i));
				} else {
					obj.put(column_name, rs.getString(i));
				}
			}
			json.put(obj);
		}
		return json;
	}
	
	/**
	 * Original version from Ian F. Darwin on <code><a href="http://www.java2s.com/Code/Java/Database-SQL-JDBC/PrintResultSetinHTML.htm"> Java2s.com </a> <br></code><p> 
	 * @see <a href="http://www.java2s.com/Code/Java/Database-SQL-JDBC/PrintResultSetinHTML.htm">http://www.java2s.com/Code/Java/Database-SQL-JDBC/PrintResultSetinHTML.htm</a>
	 * @author Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002. - Original
	 * @param rs the input ResultSet
	 * @return a String that represents the whole ResultSet in a table in HTML format 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String toHTML (final ResultSet rs) throws IOException, SQLException {

		if (rs== null)
			return StringUtils.EMPTY_STRING;
		else if (rs.isClosed() || !rs.next())
			return StringUtils.EMPTY_STRING;
	
		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		StringBuffer buf = new StringBuffer();

		buf.append("<P ALIGN='center'><table border='1' cellpadding='3' cellspacing='1' width = '100%'>\n");
//		buf.append("<table border=1>\n");

		// Header = Column Names
		buf.append("<tr>");
		for (int i=1; i<=count; i++) {
/*/			long size = md.getColumnDisplaySize(i);
			long colSize = Math.min(size*6, 400);
			buf.append("<th width = '"  + colSize + "px'>");
/**/
			buf.append("<th>");
			buf.append(md.getColumnLabel(i));
			buf.append("</th>"); // is it optional????
		}
		buf.append("</tr>\n");

		// Record lines = Field values
		rs.beforeFirst();
		while (!rs.isClosed() && rs.next()) {
			buf.append("<tr>");
			for (int i=1; i<=count; i++) {
				buf.append("<td>");
				buf.append(rs.getString(i));
				buf.append("</td>"); // is it optional????
			}
			buf.append("</tr>\n");
		}

		buf.append("</table>\n");
		return buf.toString();
	}
}