package mb.datalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * 
 * For writing CSV data built from the CSVParamBuilder to a specified file.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class CSVWriter {
	
	/**
	 * Convenience method for writing CSV data to a File object.
	 * @param file
	 * @param builder
	 * @throws IOException
	 */
	public static void writeToCSVFile( File file, CSVParamBuilder builder ) throws IOException {
		FileOutputStream fileOS = new FileOutputStream( file );
		writeToCSVOutputStream( fileOS, builder );
	}
	
	/**
	 * Writes CSV data to the specified output stream.
	 * @param outputStream
	 * @param builder
	 * @throws IOException
	 */
	public static void writeToCSVOutputStream( OutputStream outputStream,
			CSVParamBuilder builder ) throws IOException {
		
		StringBuilder output = new StringBuilder();
		Joiner joiner = Joiner.on( builder.columnSeparator );
		output.append( joiner.join( builder.headerNames ) ).append( System.lineSeparator() );
		for ( List<Object> row : builder.data ) {
			output.append( joiner.join( row ) ).append( System.lineSeparator() );
		}
		
		try ( OutputStreamWriter osWriter = new OutputStreamWriter( outputStream ); ) {
			osWriter.write( output.toString() );
		}
	}
	
	/**
	 * Specifies parameters for the CSVWriter, including data to write.
	 */
	public static class CSVParamBuilder {
		private char				columnSeparator	= ',';
		private List<String>		headerNames		= new LinkedList<>();
		private List<List<Object>>	data			= new LinkedList<>();
		
		/**
		 * Sets the delimiter to use (default ',')
		 * @param columnSeparator
		 * @return
		 */
		public CSVParamBuilder setColumnSeparator( char columnSeparator ) {
			this.columnSeparator = columnSeparator;
			return this;
		}
		
		/**
		 * Sets column names (default: none)
		 * @param headerNames
		 * @return
		 */
		public CSVParamBuilder setHeaderNames( List<String> headerNames ) {
			this.headerNames = headerNames;
			return this;
		}
		
		/**
		 * Sets all data from one nested List<List<Object>>.
		 * @param data
		 * @return
		 */
		public CSVParamBuilder setData( List<List<Object>> data ) {
			this.data = data;
			return this;
		}
		
		/**
		 * Adds a single row of data.
		 * @param data
		 * @return
		 */
		public CSVParamBuilder addDataRow( List<Object> data ) {
			this.data.add( data );
			return this;
		}
		
		/**
		 * Adds a single datum.
		 * @param data
		 * @return
		 */
		public CSVParamBuilder addSingleDatum( Object data ) {
			this.data.add( Lists.newArrayList( data ) );
			return this;
		}
		
		/**
		 * Adds a single header.
		 * @param header
		 * @return
		 */
		public CSVParamBuilder addHeader( String header ) {
			this.headerNames.add( header );
			return this;
		}
	}
}
