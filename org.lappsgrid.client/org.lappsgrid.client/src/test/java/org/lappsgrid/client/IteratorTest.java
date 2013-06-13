package org.lappsgrid.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorTest
{

   public IteratorTest()
   {
   }

   public void run() {
      List<String> list = new ArrayList<String>();
      for (int i = 0; i < 10; ++i) {
         list.add("Foo");
      }
         
      Iterator<String> iterator = list.iterator();
      for (int i = 0; i < 20; ++i) {
         System.out.println( i + " " + iterator.next());
      }
   }
   
   public static void main(String[] args) {
      new IteratorTest().run();
   }
}
