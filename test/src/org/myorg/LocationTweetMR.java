package org.myorg;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.*;
import org.myorg.WordCount.Map;
import org.myorg.WordCount.Reduce;
import org.myorg.model.key.Location;
import org.myorg.model.key.Tweet;

public class LocationTweetMR {

	public static class Map extends Mapper<LongWritable, Text, Location, Tweet> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			System.out.println(line);
			Location location = new Location();
			String[] inputArray = line.split(",");
			String coordinate = inputArray[15];
			coordinate = coordinate.replace(",", " ").replace("\"", "").trim();
			location.setCoordinate(coordinate);

			Tweet tweet = new Tweet();
			String text = inputArray[3].replace("\"", "'").trim();
			String user = inputArray[13].trim();
/*mADE SOME CHANGES*/
			tweet.setText(text);
			tweet.setUser(user);
			context.write(location, tweet);
		}
	}

	public static class Reduce extends Reducer<Location, Tweet, Location, Text> {

		public void reduce(Location key, Iterator<Tweet> values, Context context)
				throws IOException, InterruptedException {

			StringBuilder valueText = new StringBuilder("[");
			while (values.hasNext()) {
				Tweet tweet = values.next();
				valueText.append("{");
				valueText.append(tweet.toString());
				valueText.append("}");
			}
			valueText.append("]");

			context.write(key, new Text(valueText.toString()));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "TwitterLccationMR");
		job.setOutputKeyClass(Location.class);
		job.setOutputValueClass(Tweet.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
