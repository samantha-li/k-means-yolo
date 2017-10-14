/* BBox.java by Samantha Li */
import java.io.*;
import java.util.Scanner;


public class BBox
{
   private final String c; // object class
   private final double w; // width of bounding box
   private final double h; // height of bounding box
   private int cluster; // cluster number
   private double IOU;

   public BBox(String c, double w, double h)
   {
      this.c = c;
      this.w = w;
      this.h = h;
      this.cluster = -1;
      IOU = Double.NEGATIVE_INFINITY;
   }

   public double w() { return w; }

   public double h() { return h; }

   public int cluster() { return cluster; }

   public String c() { return c; }

   public double IOU() { return IOU; }

   public void setIOU(double iou) { this.IOU = iou; }

   public void setCluster(int cluster)
   {
      this.cluster = cluster;
   }

}
