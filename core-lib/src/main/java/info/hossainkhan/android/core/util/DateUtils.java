/*
 * MIT License
 *
 * Copyright (c) 2016 Hossain Khan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.hossainkhan.android.core.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateUtils {

    public static String getElapsedTime(String dateTimeString) {
        final int MAX_SEGMENT = 2;
        DateTime myBirthDate = new DateTime(dateTimeString);
        Period period = new Period(myBirthDate, DateTime.now());

        int segmentCount = 0;

        PeriodFormatterBuilder formatterBuilder = new PeriodFormatterBuilder();

        if (period.getYears() > 0) {
            formatterBuilder.appendYears().appendSuffix("y")
                    .appendSeparator(", ");
            segmentCount++;
        }

        if (period.getMonths() > 0) {
            formatterBuilder.appendMonths().appendSuffix("m")
                .appendSeparator(", ");
            segmentCount++;
        }

        if (segmentCount < MAX_SEGMENT && period.getWeeks() > 0) {
            formatterBuilder.appendWeeks().appendSuffix("w")
                    .appendSeparator(", ");
            segmentCount++;
        }
        if (segmentCount < MAX_SEGMENT && period.getDays() > 0) {
            formatterBuilder.appendDays().appendSuffix("d")
                    .appendSeparator(", ");
            segmentCount++;
        }
        if (segmentCount < MAX_SEGMENT && period.getHours() > 0) {
            formatterBuilder.appendHours().appendSuffix("h")
                    .appendSeparator(", ");
            segmentCount++;
        }
        if (segmentCount < MAX_SEGMENT && period.getMinutes() > 0) {
            formatterBuilder.appendMinutes().appendSuffix("m");
            segmentCount++;
        }

        if(segmentCount > 0) {
            formatterBuilder.appendSuffix(" ago");
        }

        formatterBuilder.printZeroNever();


        PeriodFormatter formatter = formatterBuilder.toFormatter();
        String elapsed = formatter.print(period);
        return elapsed;
    }


}
