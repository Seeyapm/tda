package org.myorg;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.json.*;

public class JsonParse {
	public static class Map extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String author;
			String line = value.toString();
			System.out.println("For the record: " + line);
			String[] tuple = line.split("\\n");
			try {
				for (int i = 0; i < tuple.length; i++) {
					JSONObject obj = new JSONObject(tuple[i]);
					author = obj.getString("author");
					System.out.println("we get the author as : " + author);
					context.write(new Text(author), new IntWritable(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		/*
		 * public void map(LongWritable key, Text value, Context context) throws
		 * IOException, InterruptedException{
		 * 
		 * String author; String book; String line = value.toString(); String[]
		 * tuple = line.split("\\n"); try{ for(int i=0;i<tuple.length; i++){
		 * JSONObject obj = new JSONObject(tuple[i]); author =
		 * obj.getString("author"); book = obj.getString("book");
		 * context.write(new Text(author), new IntWritable(1)); }
		 * }catch(JSONException e){ e.printStackTrace(); } }
		 */
	}

	public static class Reduce extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text arg0, Iterator<IntWritable> arg1,
				Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int sum = 0;
			System.out.println("For author :"+arg0.toString());
			while (arg1.hasNext()) {
				int value =arg1.next().get();
				System.out.println("We get value as : "+value);
				sum +=value;
			}
			arg2.write(arg0, new IntWritable(sum));

		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		if (args.length != 2) {
			System.err.println("Usage: CombineBooks <in> <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "JsonParse");
		job.setJarByClass(JsonParse.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
