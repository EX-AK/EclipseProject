package exousia.greenladlemain;

import java.util.Calendar;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import android.support.v4.app.Fragment;
import android.util.Log;

public class UniversalCalendarAndTimeClass
		implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	private Calendar calendar;	
	Fragment fragment;
	
	DeliveryDetail deliveryDetail;

	public UniversalCalendarAndTimeClass(DeliveryDetail deliveryDetail) {
		// TODO Auto-generated constructor stub
		
		this.deliveryDetail=deliveryDetail;		
		calendar = Calendar.getInstance();

	}


	@Override
	public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		calendar.set(year, monthOfYear, dayOfMonth);
		deliveryDetail.dateSelected(calendar);
	}

	public void chooseDate() {

		DatePickerDialog dialog=DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		//calendar.set(Calendar.DAY_OF_MONTH,Calendar.MONTH,Calendar.YEAR);
		
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));		
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		Log.e("curr_date",calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.DAY_OF_MONTH));
		dialog.setMinDate(calendar);
		Calendar calendar2=Calendar.getInstance();
		//calendar.setTime(Calendar.DAY_OF_MONTH,Calendar.MONTH+1,Calendar.YEAR);
		calendar2.set(Calendar.YEAR,calendar.get(Calendar.YEAR));		
		calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
		calendar2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		Log.e("next_date",calendar2.get(Calendar.YEAR)+" "+calendar2.get(Calendar.MONTH)+" "+calendar2.get(Calendar.DAY_OF_MONTH));
		dialog.setMaxDate(calendar2);
		dialog.show(deliveryDetail.getFragmentManager(), "datePicker");
	}



	public interface DateAndTimePicker {
		public void dateSelected(Calendar calendar);

		
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}
	
}
