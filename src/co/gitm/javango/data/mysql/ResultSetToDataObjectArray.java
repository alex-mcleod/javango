package co.gitm.javango.data.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import co.gitm.javango.data.DataObject;
import co.gitm.javango.data.DataObjectArray;

/**
 * @author Alex McLeod
 * Modification of code from http://stackoverflow.com/questions/6514876/most-effecient-conversion-of-resultset-to-json
 * 
 * Converts the ResultSet object returned by a MySqlDataSource to  a DataObject array to be used by Javango. 
 *
 */
public class ResultSetToDataObjectArray {
	public static DataObjectArray convert( ResultSet rs ) throws SQLException {
		DataObjectArray doa = new DataObjectArray();
		ResultSetMetaData rsmd = rs.getMetaData();

		while(rs.next()) {
			int numColumns = rsmd.getColumnCount();
			DataObject obj = new DataObject();

			for (int i=1; i<numColumns+1; i++) {
				String column_name = rsmd.getColumnName(i);

				if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
					obj.put(column_name, rs.getArray(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
					obj.put(column_name, rs.getInt(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
					obj.put(column_name, rs.getBoolean(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
					obj.put(column_name, rs.getBlob(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
					obj.put(column_name, rs.getDouble(column_name)); 
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
					obj.put(column_name, rs.getFloat(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
					obj.put(column_name, rs.getInt(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
					obj.put(column_name, rs.getNString(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
					obj.put(column_name, rs.getString(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
					obj.put(column_name, rs.getInt(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
					obj.put(column_name, rs.getInt(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
					obj.put(column_name, rs.getDate(column_name));
				}
				else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
					obj.put(column_name, rs.getTimestamp(column_name));   
				}
				else{
					obj.put(column_name, rs.getObject(column_name));
				}
			}
			doa.add(obj);
		}
		return doa;
	}
}

