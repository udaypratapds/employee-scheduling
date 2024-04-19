package org.acme.employeescheduling.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.employeescheduling.domain.*;

import java.io.File;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DemoDataGenerator {

//    public static void main(String[] args){
//        logger.setLevel(Level.INFO);
//        ConsoleHandler consoleHandler = new ConsoleHandler();
//        logger.addHandler(consoleHandler);
//
//        DemoDataGenerator d = new DemoDataGenerator();
//        d.generateDemoData();
//    }

    private static final Logger logger = Logger.getLogger(DemoDataGenerator.class.getName());

    public void addDraftShifts(EmployeeSchedule schedule) {
    }

    public enum DemoData {
        SMALL,
        LARGE
    }
    private static final String[] LOCATIONS = { "Fruit-dept.","Bazar-Service"};
//    private static final String[] EMP_NAMES = { "Amy", "Beth", "Chad", "Dan", "Elsa", "Flo"};
//    private static final String[] empSkills = { "Chopper", "Chopper", "Washer", "Washer", "Seller", "Seller"};

//    private static final String[] REQUIRED_SKILLS = { "Seller", "General Staff" };
//    private static final String[] OPTIONAL_SKILLS = { "Chopper", "Washer" };

    private static final Duration SHIFT_LENGTH = Duration.ofHours(7);
    private static final LocalTime MORNING_SHIFT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime AFTERNOON_SHIFT_START_TIME = LocalTime.of(15, 0);

    static final LocalTime[][] SHIFT_START_TIMES_COMBOS = {
            { MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME },
    };

    Map<String, List<LocalTime>> locationToShiftStartTimeListMap = new HashMap<>();

    public static  void main(String[] args){
        logger.setLevel(Level.INFO);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);

        DemoDataGenerator d = new DemoDataGenerator();
        d.generateDemoData();
    }
    public EmployeeSchedule generateDemoData() {

        ObjectMapper objMapper = new ObjectMapper();

        List<Employee> employeeList = null;
        try {
            File file = new File("D:\\DS\\Scheduling Project\\employee-scheduling\\src\\main\\resources\\test.json");
            if (file.exists()) {
                Employee[] employees = objMapper.readValue(file, Employee[].class);
                employeeList = Arrays.asList(employees);
//                logger.log(Level.INFO, "File read successfully");
                logger.log(Level.INFO, employeeList.toString());
                for (Employee employee : employeeList) {
                    logger.log(Level.INFO, employee.getName());
                    logger.log(Level.INFO, employee.getPosition());
                    List<Schedule> schedules = employee.getSchedules();
                    logger.log(Level.INFO,schedules.toString());
                    for (Schedule schedule : schedules) {
                        List<Eshift> shifts = schedule.getSchedule();
                        for (Eshift shift : shifts) {
                            logger.log(Level.INFO,shift.toString());
                            logger.log(Level.INFO,shift.getDays().toString());
                            logger.log(Level.INFO,shift.getStart_time());
                            logger.log(Level.INFO,shift.getEnd_time());
                        }
                    }
                }
            } else {
                logger.log(Level.INFO, "File not found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();

        int initialRosterLengthInDays = 14;

        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

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
            logger.log(Level.INFO,"Loc to shift map"+locationToShiftStartTimeListMap);
        }

        List<Employee> employees = new ArrayList<>();

        for (Employee emp : employeeList){
            Set<String> skills = new HashSet<>();
            skills.add(emp.getPosition());
            Employee employee = new Employee(emp.getName(), skills);
            employees.add(employee);
        }
            /*for (int i = 0; i < EMP_NAMES.length; i++) {
                Set<String> skills = new HashSet<>();
                skills.add(empSkills[i]);
                Employee employee = new Employee(EMP_NAMES[i], skills);
                employees.add(employee);
            }*/

        employeeSchedule.setEmployees(employees);

        List<Availability> availabilities = new LinkedList<>();
        List<Shift> shifts = new LinkedList<>();

        shifts.addAll(generateShiftsForWeek(currentDate, random));

        shifts.addAll(generateShiftsForDays(currentDate.plusDays(7), initialRosterLengthInDays - 7, random));

//        int count = 0;
//        for (int i = 0; i < initialRosterLengthInDays; i++) {
//            LocalDate date = currentDate.plusDays(i);
//            if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
//                Set<Employee> employeesWithAvailabilitiesOnDay = pickSubset(employees, random, 5);
//                logger.log(Level.INFO,"employeesWithAvailabilitiesOnDay--------------->" +employeesWithAvailabilitiesOnDay.toString());
//                for (Employee employee : employeesWithAvailabilitiesOnDay) {
//                    AvailabilityType availabilityType = pickRandom(AvailabilityType.values(), random);
////                    logger.log(Level.INFO, availabilityType.toString()+"availabilityType");
//                    availabilities.add(new Availability(Integer.toString(count++), employee, date, availabilityType));
//                }
//                shifts.addAll(generateShiftsForDay(date, random));
//            }
//        }
        AtomicInteger countShift = new AtomicInteger();
        logger.log(Level.INFO,"Count shift"+countShift);
        shifts.forEach(s -> s.setId(Integer.toString(countShift.getAndIncrement())));
        employeeSchedule.setAvailabilities(availabilities);
        employeeSchedule.setShifts(shifts);

//        logger.log(Level.INFO,"Employee schedule Employees-------------------->"+employeeSchedule.getEmployees());
//        logger.log(Level.INFO,"Employee schedule Shifts-------------------->"+employeeSchedule.getShifts());
//        logger.log(Level.INFO,"Employee schedule Availabilities-------------------->"+employeeSchedule.getAvailabilities());

        return employeeSchedule;
    }

    private List<Shift> generateShiftsForWeek(LocalDate startDate, Random random) {
        List<Shift> shifts = new LinkedList<>();
        for (LocalDate date = startDate; date.isBefore(startDate.plusDays(7)); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                shifts.addAll(generateShiftsForDay(date, random));
            }
        }
        return shifts;
    }
    private List<Shift> generateShiftsForDays(LocalDate startDate, int numDays, Random random) {
        List<Shift> shifts = new LinkedList<>();
        for (int i = 0; i < numDays; i++) {
            LocalDate date = startDate.plusDays(i);
            if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                shifts.addAll(generateShiftsForDay(date, random));
            }
        }
        return shifts;
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
        logger.log(Level.INFO, "===============> shifts" + shifts.toString());
        return shifts;
    }

    private List<Shift> generateShiftForTimeslot(LocalDateTime timeslotStart, LocalDateTime timeslotEnd,
                                                 String location,
                                                 Random random) {
        int shiftCount = 2;

//        if (random.nextDouble() > 0.9) {
//            // generate an extra shift
//            shiftCount++;
//        }

        List<Shift> shifts = new LinkedList<>();
        for (int i = 0; i < shiftCount; i++) {
//            String requiredSkill;
//            if (random.nextBoolean()) {
//                requiredSkill = pickRandom(REQUIRED_SKILLS, random);
//            } else {
//                requiredSkill = pickRandom(OPTIONAL_SKILLS, random);
//            }
            shifts.add(new Shift(timeslotStart, timeslotEnd, location));
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

