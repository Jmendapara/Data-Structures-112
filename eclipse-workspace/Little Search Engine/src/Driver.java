package lse;

import java.io.*;
import java.util.*;

public class Driver 
{
	static Scanner sc = new Scanner(System.in);
	
	static String getOption() 
	{
		System.out.print("getKeyWord(): ");
		String response = sc.next();
		return response;
	}
	
	public static void main(String args[])
	{
		LittleSearchEngine lse = new LittleSearchEngine();
		ArrayList<Occurrence> docs = new ArrayList<Occurrence>();
	
	/*	ArrayList<Occurrence> arr = new ArrayList<Occurrence>();
		arr.add(new Occurrence("doc1",82));
		arr.add(new Occurrence("doc2",76));
		arr.add(new Occurrence("doc3",71));
		arr.add(new Occurrence("doc4",71));
		arr.add(new Occurrence("doc5",70));
		arr.add(new Occurrence("doc6",65));
		arr.add(new Occurrence("doc7",61));
		arr.add(new Occurrence("doc8",56));
		arr.add(new Occurrence("doc9",54));
		arr.add(new Occurrence("doc10",51));
		arr.add(new Occurrence("doc11",48));
		arr.add(new Occurrence("doc12",45));
		arr.add(new Occurrence("doc13",41));
		arr.add(new Occurrence("doc14",36));
		arr.add(new Occurrence("doc15",34));
		arr.add(new Occurrence("doc16",30));
		arr.add(new Occurrence("doc17",25));
		arr.add(new Occurrence("doc18",20));
		arr.add(new Occurrence("doc19",20));
		arr.add(new Occurrence("doc20",18));
		arr.add(new Occurrence("doc21",17));
		arr.add(new Occurrence("doc21",17));
		arr.add(new Occurrence("doc21",14));
		arr.add(new Occurrence("doc21",12));
		arr.add(new Occurrence("doc21",17));


		
		System.out.println(lse.insertLastOccurrence(arr));
		*/
		try
		{
			lse.makeIndex("docs.txt", "noisewords.txt");
		} 
		catch (FileNotFoundException e)
		{
		}		
		
//		String input;
//
		for (String hi : lse.keywordsIndex.keySet())
			System.out.print(hi+" "+lse.keywordsIndex.get(hi));
//				
//		while (!(input = getOption()).equals("q"))
//		{
//				System.out.println(lse.getKeyWord(input));
//		}
		
		System.out.println();
		System.out.println(lse.top5search("deep", "world"));
	}
}