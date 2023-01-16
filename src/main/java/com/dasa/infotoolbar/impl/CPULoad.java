package com.dasa.infotoolbar.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class CPULoad {

	 private static long prev_idle;
	 private static long prev_total;
	    
	    public static float getCPUProcOrig() {
	        BufferedReader cpuReader = null;
	        float usage = -1.0f;
	        try {
	            cpuReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")));
	            final String cpuLine = cpuReader.readLine();
	            if (cpuLine == null) {
	                System.out.println("/proc/stat didn't work well");
	            }
	            else {
	                final String[] cpu_cols = cpuLine.split("\\s+");
	                final long idle = Long.parseLong(cpu_cols[4]);
	                long total = 0L;
	                for (int i = 1; i < cpu_cols.length; ++i) {
	                    total += Long.parseLong(cpu_cols[i]);
	                }
	                final long diff_idle = idle - CPULoad.prev_idle;
	                final long diff_total = total - CPULoad.prev_total;
	                final long diff_used = diff_total - diff_idle;
	                usage = 100.0f * diff_used / diff_total;
	                CPULoad.prev_total = total;
	                CPULoad.prev_idle = idle;
	            }
	        }
	        catch (Exception e) {
	            System.out.println("Exception '" + e.getMessage() + "'occured with trace: " + e.getStackTrace());
	        }
	        finally {
	            if (cpuReader != null) {
	                try {
	                    cpuReader.close();
	                }
	                catch (IOException ex) {}
	            }
	        }
	        if (cpuReader != null) {
	            try {
	                cpuReader.close();
	            }
	            catch (IOException ex2) {}
	        }
	        return usage;
	    }
}
