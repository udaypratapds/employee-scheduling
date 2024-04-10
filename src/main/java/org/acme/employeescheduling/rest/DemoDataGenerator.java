package org.acme.employeescheduling.rest;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.employeescheduling.domain.Availability;
import org.acme.employeescheduling.domain.AvailabilityType;
import org.acme.employeescheduling.domain.Employee;
import org.acme.employeescheduling.domain.EmployeeSchedule;
import org.acme.employeescheduling.domain.ScheduleState;
import org.acme.employeescheduling.domain.Shift;

@ApplicationScoped
public class DemoDataGenerator {

    public static  void main(String[] args){
        logger.setLevel(Level.INFO);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);

        DemoDataGenerator d = new DemoDataGenerator();
        d.generateDemoData();
    }

    private static final Logger logger = Logger.getLogger(DemoDataGenerator.class.getName());

    public void addDraftShifts(EmployeeSchedule schedule) {
    }

    public enum DemoData {
        SMALL,
        LARGE
    }

    private static final String[] FIRST_NAMES = { "Amy", "Beth", "Chad", "Dan", "Elsa", "Flo" };
    private static final String[] REQUIRED_SKILLS = { "Seller", "General Staff" };
    private static final String[] OPTIONAL_SKILLS = { "Chopper", "Washer" };
    private static final String[] LOCATIONS = { "Fruit-Store", "Meat-Store" };
    private static final Duration SHIFT_LENGTH = Duration.ofHours(7);
    private static final LocalTime MORNING_SHIFT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime AFTERNOON_SHIFT_START_TIME = LocalTime.of(15, 0);

    static final LocalTime[][] SHIFT_START_TIMES_COMBOS = {
            { MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME },
    };

    Map<String, List<LocalTime>> locationToShiftStartTimeListMap = new HashMap<>();

    public EmployeeSchedule generateDemoData() {
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();

        int initialRosterLengthInDays = 30;

        LocalDate currentDate = LocalDate.now();
        //LocalDate startDate = currentDate;
       // LocalDate startDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        ScheduleState scheduleState = new ScheduleState();
        scheduleState.setFirstDraftDate(currentDate);
        scheduleState.setDraftLength(initialRosterLengthInDays);
        scheduleState.setPublishLength(7);
        scheduleState.setLastHistoricDate(currentDate.minusDays(7));

        employeeSchedule.setScheduleState(scheduleState);

        Random random = new Random(0);

        int shiftTemplateIndex = 0;
        for (String location : LOCATIONS) {
            locationToShiftStartTimeListMap.put(location, List.of(SHIFT_START_TIMES_COMBOS[shiftTemplateIndex]));
            shiftTemplateIndex = (shiftTemplateIndex + 1) % SHIFT_START_TIMES_COMBOS.length;
        }

        List<Employee> employees = new ArrayList<>();
        for(int i = 0; i < FIRST_NAMES.length; i++) {
            Set<String> skills = pickSubset(List.of(OPTIONAL_SKILLS), random, 3, 1);
            skills.add(pickRandom(REQUIRED_SKILLS, random));
            Employee employee = new Employee(FIRST_NAMES[i], skills);
            employees.add(employee);
        }

//        for(Employee employee : employees){
//            logger.log(Level.INFO, employee.getName());
//            logger.log(Level.INFO, employee.getSkills());
//        }

//        for (Employee employee : employees) {
//            logger.log(Level.INFO, employee.getName());
//            logger.log(Level.INFO, employee.getSkills().toString());
//        }

        logger.log(Level.INFO, employees.toString());

//        logger.log(Level.INFO, "kjdsfghjkghlkjfhd");
        employeeSchedule.setEmployees(employees);

        List<Availability> availabilities = new LinkedList<>();
        List<Shift> shifts = new LinkedList<>();
        int count = 0;
        for (int i = 0; i < initialRosterLengthInDays; i++) {
            LocalDate date = currentDate.plusDays(i);
            if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                Set<Employee> employeesWithAvailabilitiesOnDay = pickSubset(employees, random, 4, 3, 2, 1);
                logger.log(Level.INFO, employeesWithAvailabilitiesOnDay.toString()+"employeesWithAvailabilitiesOnDay");
                for (Employee employee : employeesWithAvailabilitiesOnDay) {
                    AvailabilityType availabilityType = pickRandom(AvailabilityType.values(), random);
                    logger.log(Level.INFO, availabilityType.toString()+"availabilityType");
                    availabilities.add(new Availability(Integer.toString(count++), employee, date, availabilityType));
                }
                shifts.addAll(generateShiftsForDay(date, random));
            }
        }
        AtomicInteger countShift = new AtomicInteger();
        shifts.forEach(s -> s.setId(Integer.toString(countShift.getAndIncrement())));
        employeeSchedule.setAvailabilities(availabilities);
        employeeSchedule.setShifts(shifts);

        return employeeSchedule;
    }

    private List<Shift> generateShiftsForDay(LocalDate date, Random random) {
        List<Shift> shifts = new LinkedList<>();
        for (String location : LOCATIONS) {
            List<LocalTime> shiftStartTimes = locationToShiftStartTimeListMap.get(location);
            for (LocalTime shiftStartTime : shiftStartTimes) {
                LocalDateTime shiftStartDateTime = date.atTime(shiftStartTime);
                LocalDateTime shiftEndDateTime = shiftStartDateTime.plus(SHIFT_LENGTH);
                shifts.addAll(generateShiftForTimeslot(shiftStartDateTime, shiftEndDateTime, location, random));
            }
        }
        return shifts;
    }

    private List<Shift> generateShiftForTimeslot(LocalDateTime timeslotStart, LocalDateTime timeslotEnd,
                                                 String location,
                                                 Random random) {
        int shiftCount = 1;

        if (random.nextDouble() > 0.9) {
            // generate an extra shift
            shiftCount++;
        }

        List<Shift> shifts = new LinkedList<>();
        for (int i = 0; i < shiftCount; i++) {
            String requiredSkill;
            if (random.nextBoolean()) {
                requiredSkill = pickRandom(REQUIRED_SKILLS, random);
            } else {
                requiredSkill = pickRandom(OPTIONAL_SKILLS, random);
            }
            shifts.add(new Shift(timeslotStart, timeslotEnd, location, requiredSkill));
        }
        return shifts;
    }

    private <T> T pickRandom(T[] source, Random random) {
        return source[random.nextInt(source.length)];
    }

    private <T> Set<T> pickSubset(List<T> sourceSet, Random random, int... distribution) {
        int probabilitySum = 0;
        for (int probability : distribution) {
            probabilitySum += probability;
        }
        int choice = random.nextInt(probabilitySum);
        int numOfItems = 0;
        while (choice >= distribution[numOfItems]) {
            choice -= distribution[numOfItems];
            numOfItems++;
        }
        List<T> items = new ArrayList<>(sourceSet);
        Collections.shuffle(items, random);
        return new HashSet<>(items.subList(0, numOfItems + 1));
    }
}

