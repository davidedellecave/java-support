package ddc.support.util;

import java.util.Date;

@Deprecated
public class DateRange {
	public Date start;
	public Date end;
	
	public DateRange() {
		
	}

	public DateRange(long start, long end) {
		this.start=new Date(start);
		this.end=new Date(end);		
	}

	public DateRange(Date start, Date end) {
		this.start=start;
		this.end=end;		
	}
	
	public DateRange(LRange range) {
		this.start=new Date(range.getLower());
		this.end=new Date(range.getUpper());		
	}

	public boolean contains(Date date, boolean includeBound) {
		if (includeBound) {
			return start.getTime()<=date.getTime() && date.getTime()<=end.getTime();
		} else {
			return start.getTime()<date.getTime() && date.getTime()<end.getTime();
			
		}
	}

	public boolean contains(long timestamp, boolean includeBound) {
		return contains(new Date(timestamp), includeBound);
	}

	@Override
	public String toString() {
		return " x IN (" +   DateUtil.formatForISO(start) + "," + DateUtil.formatForISO(end) + ")"; 
	}
}

/*
 *  public class DateRange {
        public DateTime begin { get; private set; }
        public DateTime end { get; private set; }
        protected LRange range { get; private set; }

        long getLongDate(DateTime dateTime) {
            String y = String.Format("{0:yyyy}", dateTime);
            y += String.Format("{0:MM}", dateTime);
            y += String.Format("{0:dd}", dateTime);
            return long.Parse(y);
        }

        public DateRange(DateTime begin, DateTime end) {
            this.begin = begin;
            this.end = end;
            range = new LRange(getLongDate(begin), getLongDate(end));
        }

        private DateRange(LRange range) {
            this.range = range;
        }

        public DateRange intersection(DateRange range) {
            LRange r = this.range.intersection(range.range);
            return new DateRange(r);
        }

        public bool isEmpty() {
            return range.isEmpty();
        }

        public bool isNotEmpty() {
            return range.isNotEmpty();
        }

        public static int getLastDayOfMounth(DateTime date) {
            DateTime last = new DateTime(date.Year, date.Month, 1).AddMonths(1).AddDays(-1);
            return last.Day;
        }

        /// <summary>
        /// Process current Date in order to return a DateRange
        /// such that DateRange.begin is the first day of month and DateRange.end is the last day of month
        /// </summary>
        /// <param name="dt"></param>
        /// <returns></returns>
        public static DateRange getDaysRangeByMounth(DateTime dt) {
            int startDay = 1;
            int endDay = getLastDayOfMounth(dt);
            DateTime startDate = new DateTime(dt.Year, dt.Month, startDay, 0, 0, 0);
            DateTime endDate = new DateTime(dt.Year, dt.Month, endDay, 23, 59, 59);
            return new DateRange(startDate, endDate);
        }


        /// <summary>
        /// Process current Date in order to return a DateRange such that
        /// DateRange.begin is the first mounth of year and DateRange.end is the last mounth of year
        /// </summary>
        /// <param name="dt"></param>
        /// <returns></returns>
        public static DateRange getMounthsRangeByYear(DateTime dt) {
            DateTime startDate = new DateTime(dt.Year, 1, 1, 0, 0, 0);
            DateTime endDate = new DateTime(dt.Year, 12, 31, 23, 59, 59);
            return new DateRange(startDate, endDate);
        }

        /// <summary>
        /// Process current Date in order to return a DateRange such that DateRange.begin is the first day of week and DateRange.end is the last day of week
        /// </summary>
        /// <param name="dt"></param>
        /// <returns></returns>
        public static DateRange getWeekRange(DateTime dt) {
            //System.Globalization.CultureInfo ci = System.Threading.Thread.CurrentThread.CurrentCulture;
            //DayOfWeek fdow = ci.DateTimeFormat.FirstDayOfWeek;
            DayOfWeek fdow = DayOfWeek.Monday;
            DateTime startDate = dt.AddDays(-(DateTime.Today.DayOfWeek - fdow));
            DateTime endDate = startDate.AddDays(6);
            return new DateRange(startDate, endDate);
        }

        public override string ToString() {
            return begin.ToString() + " - " + end.ToString();
        }

        public string To_YYYYMMdd() {
            return String.Format("{0:yyyy-MM-dd}", begin) + " - " + String.Format("{0:yyyy-MM-dd}", end);
        }

        public string To_ddMMYYYY() {
            return String.Format("{0:dd-MM-yyyy}", begin) + " - " + String.Format("{0:dd-MM-yyyy}", end);
        }
    }
    */
