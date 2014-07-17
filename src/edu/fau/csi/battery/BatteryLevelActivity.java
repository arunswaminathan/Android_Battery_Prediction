package edu.fau.csi.battery;



import edu.fau.csi.battery.R;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;





public class BatteryLevelActivity extends Activity{
    /** Called when the activity is first created. */
    private TextView batterLevel;
    private TextView tempdisplay;
    private TextView voltdisplay;
    private TextView memdisplay;
    private TextView coresdisplay;
    private TextView workloadtimerdisplay;
    private TextView timestampdisplay;
    private TextView wifistatusdisplay;
    private TextView bluetoothstatusdisplay;
    private TextView durationdisplay;
    private Handler mTimerHandler = new Handler();
    Button btn;
    private XYPlot plot;
    private XYPlot plot2;
    private String mytime;
    double wifibattery;
    double bluetoothbattery;
    double Benchmark;
    int i=0;
    float level;
    double duration;
        List<Number>series1Numbers = new ArrayList<Number>();
    List<Number>series2Numbers = new ArrayList<Number>();
    
	
  
    
    

    @Override
    /**
     * Called when the current activity is first created.
     */
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.activity_battery_level);
        batterLevel = (TextView) this.findViewById(R.id.batterylevel);
        tempdisplay = (TextView) this.findViewById(R.id.TempDisp);
        voltdisplay = (TextView) this.findViewById(R.id.VoltDisp);
        memdisplay=(TextView) this.findViewById(R.id.availMem);
        coresdisplay=(TextView) this.findViewById(R.id.cores);
        workloadtimerdisplay=(TextView) this.findViewById(R.id.wTimer);
        timestampdisplay = (TextView) this.findViewById(R.id.timestamp);
        wifistatusdisplay= (TextView) this.findViewById(R.id.wifistatus);
        bluetoothstatusdisplay=(TextView) this.findViewById(R.id.bluetoothstatus);
        durationdisplay = (TextView) this.findViewById(R.id.duration);
        
        
        new AlertDialog.Builder(this)
        .setTitle("Important Info")
        .setMessage("This app launches a new tab in the browser from time to time to run a set of workload tests and report the battery status. " +
        		"Are you sure you want to run the app?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
               
            	
            	Timer timer1 = new Timer();
                
                timer1.scheduleAtFixedRate(new TimerTask()
                {
                	
                
                	public void run()
                    	{
        BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        		   public void run()
        		   {
        			new CountDownTimer(10000, 1000) {

        	     public void onTick(final long millisUntilFinished) {
        	    	 BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        	  		   public void run()
        	  		   {
        	  			 workloadtimerdisplay.setVisibility(View.VISIBLE);
        	  			   workloadtimerdisplay.setText("Running next workload test in: " + millisUntilFinished / 1000 + "...");
        	  		   }
        	    	 });
        	  		   
        	     }
        	     
        	    

        	     public void onFinish() {
        	    	 BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        		  		   public void run()
        		  		   {
        		  			   mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        		  			  
        		  			 
        		  			   workloadtimerdisplay.setVisibility(View.INVISIBLE);
        		  			   List<String> my_words = new LinkedList<String>();
        		  		       my_words.add("http://www.google.com/");
        		  		       my_words.add("http://www.yahoo.com/");
        		  		       my_words.add("http://www.cnn.com/");
        		  		       my_words.add("http://www.facebook.com/");
        		  			   Random random = new Random(); //Create random class object
        		  			   
        		  			   	timestamp();
        	        			batteryLevel(); 
        	                    getNumCores();
        	                    readUsage();
        	                    wifistatus();
        	                    plotgraph();
        	                    bluetoothstatus();
        	                    int randomNumber = random.nextInt(my_words.size()); 
        	                    String uri1 = "http://maps.google.com/";
        	    	 			
        	                    
        	                    if (i==0)
        	                    	{
        	     			   			i=i+1;
        	                    		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(my_words.get(randomNumber))));           
        	                    	}
        	                    
        	                    else if (i==1)
        	                    	{
        	                    		i=i-1;
        	                    		startActivity(new Intent(android.content.Intent.ACTION_DEFAULT, Uri.parse(uri1)));
        	                    		
        	                    	}
        	    	
        	    	
        	    	 
        	    	 			
                 	   
                 	 
                 	   
         			   
        		  		   }
        	    	 });
        		  		   }
        	  }.start(); 
        		   }
        	  });
                		
                    	};
                   }, 1000, 45000);
               }
            
           private void timestamp(){
            	BatteryLevelActivity.this.runOnUiThread(new Runnable(){
         		   public void run(){
            	timestampdisplay.setText(mytime);
         		   }
            	});
            }
            
            private void batteryLevel() {
                BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        context.unregisterReceiver(this);
                        double avgCurr=0;
                        
                        int current = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        intent.getDoubleExtra(BatteryManager.EXTRA_SCALE, 0.5);
                        int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                        intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
                        
                        batterLevel.setText((level) + "%");
                        tempdisplay.setText(temp/10 + " C");
                        voltdisplay.setText (voltage + " mV" );
                        series2Numbers.add(current); 
                        Integer Benchmark =0;
                        if (!series2Numbers.isEmpty()) {
                        	for (Number curr : series2Numbers){
                        		Benchmark = Benchmark + (Integer) curr;
                        		
                        	}
                        	avgCurr = Benchmark.doubleValue()/series2Numbers.size();
                        }
                        
                        duration = (2.1 / ((avgCurr/10000))) * (level/100);
                        double fractionalpart = duration % 1;
                        double minutes = fractionalpart * 60;
                        double integralpart = duration - fractionalpart;
                        durationdisplay.setText ((long) integralpart + "h " + (long) minutes + "m");
                        
                        
                        
                        
                    }
                };
                IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                registerReceiver(batteryLevelReceiver, batteryLevelFilter);
            }
            
            
            	
            
            
            
           
            
           
           private void getNumCores() {
        	   
        	    class CpuFilter implements FileFilter {
        	        @Override
        	        public boolean accept(File pathname) {
        	            
        	            if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
        	                return true;
        	            }
        	            return false;
        	        }      
        	    }

        	    try {
        	        
        	        File dir = new File("/sys/devices/system/cpu/");
        	        
        	        final File[] files = dir.listFiles(new CpuFilter());
        	        
        	         BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        		   public void run(){
        	        coresdisplay.setText(files.length + "cores");
        	    }
        		   });
        	    } catch(Exception e) {
        	        
        	         BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        		   public void run(){
        	    	coresdisplay.setText("1 core");
        	    	}
        		   });
        	    }
        	   
        	   
        			   
        			
        	   }
        	   
           
             
           private float readUsage() {
        	    try {
        	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
        	        String load = reader.readLine();

        	        String[] toks = load.split(" ");

        	        long idle1 = Long.parseLong(toks[5]);
        	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
        	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

        	        try {
        	            Thread.sleep(360);
        	        } catch (Exception e) {}

        	        reader.seek(0);
        	        load = reader.readLine();
        	        reader.close();

        	        toks = load.split(" ");

        	        long idle2 = Long.parseLong(toks[5]);
        	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
        	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

        	        float cpuValue = (float) ((cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));
        	        Float.toString(cpuValue);
        	        
        	        BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        	 		   public void run(){
        	        
        	 		   }
        	        });

        	    } catch (IOException ex) {
        	        ex.printStackTrace();
        	    }
        	    
        	    MemoryInfo mi = new MemoryInfo();
        	    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        	    activityManager.getMemoryInfo(mi);
        	    final Long availableMegs = mi.availMem / 1048576;
        	    BatteryLevelActivity.this.runOnUiThread(new Runnable(){
        			   public void run(){
        	    
        	    series1Numbers.add(availableMegs);
        	    
        	    memdisplay.setText(availableMegs + " MB");
        		
        			   }
        	    });
        	   
        	    return 0;
        	} 
           
           
           private void wifistatus(){
        	   ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        	     final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        	   BatteryLevelActivity.this.runOnUiThread(new Runnable(){
          		   public void run()
          		   {
          			 wifistatusdisplay.setText("" + mWifi.getDetailedState());
          			 DetailedState wifistat = mWifi.getDetailedState();
          			 if (wifistat.toString() == "CONNECTED")
          			 {wifibattery = 0.3;}
          		  }
        	 });
        	      
        	   
        	   
        	
           }
           
           private void bluetoothstatus(){
        	   BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        	   if (bluetoothAdapter == null) {
        	               	   } else {
        	       if (!bluetoothAdapter.isEnabled()) {
        	           bluetoothstatusdisplay.setText("OFF" );
        	       }
        	       else {
        	    	   bluetoothstatusdisplay.setText("ON" );
        	    	   bluetoothbattery = 0.15;
        	       }
        	       
        	   }
        		
        	}
           
           private void plotgraph(){
        	  
        	   
        	   
        	// initialize our XYPlot reference:
        	   plot = (XYPlot) findViewById(R.id.plot);
        	   plot2 = (XYPlot) findViewById(R.id.plot);

        	  

        	   // Turn the above arrays into XYSeries':
        	   XYSeries series1 = new SimpleXYSeries(
        	   series1Numbers,          // SimpleXYSeries takes a List so turn our array into a List
        	   SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
        	   "");                             // Set the display title of the series

        	   // same as above
        	  XYSeries series2 = new SimpleXYSeries(
              series2Numbers, 
              SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "");

        	   // Create a formatter to use for drawing a series using LineAndPointRenderer
        	   // and configure it from xml:
        	   LineAndPointFormatter series1Format = new LineAndPointFormatter();
        	   series1Format.setPointLabelFormatter(new PointLabelFormatter());
        	   series1Format.configure(getApplicationContext(),
        	   R.xml.line_point_formatter_with_plf1);
        	    // add a new series' to the xyplot:
        	   		plot.addSeries(series1, series1Format);   
        		  // reduce the number of range labels
        		   plot.setTicksPerRangeLabel(3);
        		   plot.getGraphWidget().setDomainLabelOrientation(-45);
        	}
         })
        .setNegativeButton("No, Exit.", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                finish();
            }
         })
         .show();
}
}

