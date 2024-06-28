
package JCalendar;

import Model.DayStatus;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CalendarPanel extends JPanel {
    private LocalDate currentDate;
    private JLabel monthLabel;
    private JPanel calendarGrid;
    private List<DayStatus> workingDays;

    public CalendarPanel() {
        this.currentDate = LocalDate.now();
        this.workingDays = new ArrayList<>();
        setLayout(new BorderLayout());

        // Create the month label
        monthLabel = new JLabel("", SwingConstants.CENTER);
        add(monthLabel, BorderLayout.NORTH);

        // Create the calendar grid
        calendarGrid = new JPanel(new GridLayout(0, 7));
        add(calendarGrid, BorderLayout.CENTER);
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        prevButton.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });
        nextButton.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        JPanel navigationPanel = new JPanel();
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        add(navigationPanel, BorderLayout.SOUTH);
        // Initialize the calendar display
        updateCalendar();
    }

    // Set working days
    public void setWorkingDays(List<DayStatus> workingDays) {
        this.workingDays = workingDays;
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.removeAll();
        // Set the month label
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        monthLabel.setText(yearMonth.getMonth().name() + " " + yearMonth.getYear());
        // Get the number of days in the month and the first day of the month
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // Monday = 1, ..., Sunday = 7
        // Add day of week labels
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            calendarGrid.add(dayLabel);
        }
        // Add empty labels for days before the first day of the month
        for (int i = 1; i < firstDayOfWeek; i++) {
            JLabel emptyLabel = new JLabel("");
            calendarGrid.add(emptyLabel);
        }
        for (int day = 1; day <= daysInMonth; day++) {
             Cell dayButton = new Cell(String.valueOf(day));
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            for(DayStatus d : workingDays){
                if(d.getDate().isEqual(date)){
                    if(d.getStatus().equals("1")){
                       
                        dayButton.setBackground(Color.red);
                    }
                    else if(d.getStatus().startsWith("Trễ")){
                        dayButton = new Cell(String.valueOf(day) + "\n" + d.getStatus());
                        dayButton.setBackground(Color.yellow);
                    }
                    else if(d.getStatus().equals("0")){
                        dayButton.setBackground(Color.green);
                    }
                    else{
                        dayButton = new Cell(String.valueOf(day) + "\n" + d.getStatus());
                        dayButton.setBackground(Color.green);
                    }
                }
            }
           
            
            if (workingDays.contains(date)) {
                dayButton.setBackground(Color.red); // Set text color to red for working days
            }
            calendarGrid.add(dayButton);
        }
        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

   
}


