package exousia.greenladlemain;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerClass extends DialogFragment implements OnDateSetListener{

	DatePickerListener mlistener;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		final Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
        theme = android.R.style.Theme_Holo_Dialog_NoActionBar;
		setStyle(style, theme);
		DatePickerDialog dialog=new DatePickerDialog(getActivity(),this, year, month, dayOfMonth);
		dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		//c.add(Calendar.DAY_OF_MONTH, 2);
	    // Set calendar to 1 month next
	    c.add(Calendar.MONTH,1);
		dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
	    
	    
		return dialog;
	}
	
	
	
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
		c.set(Calendar.YEAR,year);
		c.set(Calendar.MONTH,monthOfYear);
		c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
		mlistener.datePicker(c);
		
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mlistener=(DatePickerListener) activity;
	}
	

	
	public interface DatePickerListener
	{
		public void datePicker(Calendar c);
	}

}
