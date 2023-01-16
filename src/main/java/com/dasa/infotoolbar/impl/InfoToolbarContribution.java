package com.dasa.infotoolbar.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;

public class InfoToolbarContribution implements SwingToolbarContribution{
	private static final int VERTICAL_SPACE = 10;
	private static final int HEADER_FONT_SIZE = 18;
	private static final int TIME_FONT_SIZE = 36;
	
	private final ToolbarContext context;
	private JLabel cpuStatus;
	private JLabel memStatus;
	private JLabel timeStatus;
	private JLabel dateStatus;
	private JLabel empty;
	private boolean viewopen;

	InfoToolbarContribution(ToolbarContext context) {
		this.context = context;
	}

	@Override
	public void openView() {
		viewopen = true;
			toolbarThread();
				
	}

	@Override
	public void closeView() {
		viewopen = false;
	}
	
	
	private void toolbarThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(viewopen) {
					//check if enabled and get robot status
					try {
						
						timeStatus.setText("<HTML>" + getTime() + "</HTML>");
						dateStatus.setText("<HTML>" + getDate() + "</HTML>");
						cpuStatus.setText("<HTML>" + getCPUStatus() + "</HTML>");	
						memStatus.setText("<HTML>" + getMEMStatus() + "</HTML>");		
						awaitTimer(400);
						
					} catch(Exception e){
						System.err.println("Something went wrong!");
					}
				}
			}
		}).start();
	}
	

	public void buildUI(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		jPanel.add(createVerticalSpace());
		jPanel.add(createInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		timeStatus = new JLabel();
		timeStatus.setText("<HTML>" + getTime() +"</HTML>");
		timeStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		timeStatus.setFont(timeStatus.getFont().deriveFont(Font.CENTER_BASELINE, TIME_FONT_SIZE));
		infoBox.add(timeStatus);
		
		dateStatus = new JLabel();
		dateStatus.setText("<HTML>" + getDate() +"</HTML>");
		dateStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		infoBox.add(dateStatus);
		
		empty = new JLabel();
		empty.setText("<HTML>" + " " +"</HTML>");
		empty.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		infoBox.add(empty);
		
		cpuStatus = new JLabel();
		cpuStatus.setText("<HTML>" + getCPUStatus() +"</HTML>");
		cpuStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		infoBox.add(cpuStatus);
		
		memStatus = new JLabel();
		memStatus.setText("<HTML>" + getMEMStatus() +"</HTML>");
		memStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		infoBox.add(memStatus);
		
		return infoBox;
	}

	private Component createVerticalSpace() {
		return Box.createRigidArea(new Dimension(0, VERTICAL_SPACE));
	}

	private String getCPUStatus() {
		int cpu = (int)CPULoad.getCPUProcOrig();
		return  String.format("CPU: %d %%", cpu);
	}
	
	private String getMEMStatus() {
		String ram = MemLoad.getMemUsage();
		return  String.format("RAM: %s", ram);
	}
	
	private String getTime() {
		Date now = new Date();
		return  String.format("Time: %tT", now);
	}
	
	private String getDate() {
		Date now = new Date();
		return  String.format("Date: %tF", now);
	}
	
	private void awaitTimer(long timeOutMilliSeconds) throws InterruptedException {
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (viewopen != false)) {
			Thread.sleep(100);
		}
	}
}
