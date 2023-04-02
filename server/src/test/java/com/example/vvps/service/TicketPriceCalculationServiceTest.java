package com.example.vvps.service;

import com.example.vvps.domain.Course;
import com.example.vvps.domain.PriceDependencies;
import com.example.vvps.domain.Station;
import com.example.vvps.domain.Train;
import com.example.vvps.error.NotFoundException;
import com.example.vvps.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


class TicketPriceCalculationServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TicketPriceCalculationService ticketPriceCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPricePerCourseWithExistingCourse() {
        List<Course> courses = getListOfCourses();
        when(courseRepository.findAll()).thenReturn(courses);
        double price = ticketPriceCalculationService.getPricePerCourse(Station.PLOVDIV, Station.ASENOVGRAD);
        assertEquals(1, price);
    }

    @Test
    void testGetPricePerCourseWithNonExistingCourse() {
        List<Course> courses = getListOfCourses();
        when(courseRepository.findAll()).thenReturn(courses);
        NotFoundException t = assertThrows(NotFoundException.class, ()->
                ticketPriceCalculationService.getPricePerCourse(Station.VARNA, Station.DOBRICH));
        assertEquals(String.format("No such course %s -> %s", "VARNA", "DOBRICH"), t.getError());
    }

    @ParameterizedTest
    @MethodSource("getDifferentStationCombinations")
    void testGetInitialTicketPrice(Station departure, Station arrival, Train train, int price) {
        List<Course> courses = getListOfCourses();
        when(courseRepository.findAll()).thenReturn(courses);
        double initialTicketPrice = ticketPriceCalculationService.getInitialTicketPrice(departure, arrival, train);
        assertEquals(price, initialTicketPrice);
    }

    @ParameterizedTest
    @MethodSource("getDifferentPriceDependencies")
    void testCalculateDiscountFromCards(PriceDependencies priceDependencies, int discount) {
        assertEquals(discount, ticketPriceCalculationService.calculateDiscountFromPriceDependencies(priceDependencies));
    }

    private static List<Arguments> getDifferentPriceDependencies() {
        return List.of(
                Arguments.of(new PriceDependencies(true, true, true, true, false),49),
                Arguments.of(new PriceDependencies(true, false, true, true, true),55),
                Arguments.of(new PriceDependencies(true, true, false, true, false),44),
                Arguments.of(new PriceDependencies(true, false, false, true, false),10),
                Arguments.of(new PriceDependencies(false, false, true, false, false),5),
                Arguments.of(new PriceDependencies(false, false, true, true, false),15),
                Arguments.of(new PriceDependencies(false, true, true, false, false),39)
        );
    }

    private static List<Arguments> getDifferentStationCombinations() {
        Train t = createDummyTrain();
        return List.of(
                Arguments.of(Station.SHUMEN, Station.PLOVDIV, t, 3),
                Arguments.of(Station.BURGAS, Station.VARNA, t, 1),
                Arguments.of(Station.ASENOVGRAD, Station.BURGAS,t, 1),
                Arguments.of(Station.SHUMEN, Station.VARNA,t, 6),
                Arguments.of(Station.SOFIA, Station.ASENOVGRAD,t, 2),
                Arguments.of(Station.DOBRICH, Station.VARNA,t, 7)
        );
    }

    private static List<Course> getListOfCourses() {
        Course a = new Course(Station.DOBRICH, Station.SHUMEN, 1);
        Course b = new Course(Station.SHUMEN, Station.PLEVEN, 1);
        Course c = new Course(Station.PLEVEN, Station.SOFIA, 1);
        Course d = new Course(Station.SOFIA, Station.PLOVDIV, 1);
        Course e = new Course(Station.PLOVDIV, Station.ASENOVGRAD, 1);
        Course f = new Course(Station.ASENOVGRAD, Station.BURGAS, 1);
        Course g = new Course(Station.BURGAS, Station.VARNA, 1);
        return List.of(a,b,c,d,e,f,g);
    }

    private static Train createDummyTrain() {
        List<Station> trainStationRoute = List.of(Station.DOBRICH, Station.SHUMEN, Station.PLEVEN, Station.SOFIA,
                Station.PLOVDIV, Station.ASENOVGRAD, Station.BURGAS, Station.VARNA);
        return Train.builder()
                .trainId(UUID.randomUUID())
                .departureTime(LocalDateTime.now())
                .stationRoute(trainStationRoute)
                .tickets(new ArrayList<>())
                .build();
    }
}
