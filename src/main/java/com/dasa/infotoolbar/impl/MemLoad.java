package com.dasa.infotoolbar.impl;

import java.io.IOException;
import java.util.Locale;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class MemLoad {
	
	public static String getMemUsage() {
        BufferedReader memReader = null;
        String usage_msg = "Error!";
        try {
            memReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/meminfo")));
            String line = "";
            long memtotal = 0L;
            long memfree = 0L;
            while ((line = memReader.readLine()) != null) {
                final String[] mem_cols = line.split("\\s+");
                if (mem_cols[0].equals("MemTotal:")) {
                    memtotal = Long.parseLong(mem_cols[1]);
                }
                else if (mem_cols[0].equals("MemFree:")) {
                    memfree = Long.parseLong(mem_cols[1]);
                }
                if (memtotal != 0L && memfree != 0L) {
                    break;
                }
            }
            final float usage_percent = 100.0f * (1.0f - memfree / (float)memtotal);
            System.out.println(String.format(Locale.ENGLISH, "total: %d, free: %d, percent: %05.2f, mb-used: %d", memtotal, memfree, usage_percent, (memtotal - memfree) / 1024L));
            usage_msg = String.valueOf(String.format(Locale.ENGLISH, "%05.2f", usage_percent)) + "% - " + String.format(Locale.ENGLISH, "%d MB used", (memtotal - memfree) / 1024L);
        }
        catch (Exception e) {
            System.out.println("Exception '" + e.getMessage() + "'occured with trace: " + e.getStackTrace());
        }
        finally {
            if (memReader != null) {
                try {
                    memReader.close();
                }
                catch (IOException ex) {}
            }
        }
        if (memReader != null) {
            try {
                memReader.close();
            }
            catch (IOException ex2) {}
        }
        return usage_msg;
    }
}
