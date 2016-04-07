package com.datastax.meters.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;

import com.datastax.meters.dao.TransactionDao;
import com.datastax.meters.model.Transaction;
import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;

public class SearchServiceImpl implements SearchService {

	private TransactionDao dao;
	private long timerSum = 0;
	private AtomicLong timerCount= new AtomicLong();

	public SearchServiceImpl() {		
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		this.dao = new TransactionDao(contactPointsStr.split(","));
	}	

	@Override
	public double getTimerAvg(){
		return timerSum/timerCount.get();
	}

	@Override

	public List<Transaction> getTransactionsByTagAndDate(String deviceID,  DateTime from, DateTime to) {
		
		Timer timer = new Timer();
		List<Transaction> transactions;

		transactions = dao.getTransactionsForDeviceIDTagsAndDate(deviceID, from, to);
			
		timer.end();
		timerSum += timer.getTimeTakenMillis();
		timerCount.incrementAndGet();
		return transactions;
	}
}
