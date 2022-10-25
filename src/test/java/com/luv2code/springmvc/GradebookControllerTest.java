package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;




    @Mock
    private StudentAndGradeService studentAndGradeService;

    @BeforeEach
    public void beforeEach() {
        jdbc.execute("insert into student(id,firstname,lastname,email_address)" +
                "values (60,'Eric','Roby','eric.roby@luv2code_school.com')");
    }

    @Test
    public void getStudentHttpRequest() throws Exception {
        CollegeStudent studentOne = new GradebookCollegeStudent("Erica","Vaccari",
                "erica_vaccari@liceokeplero.it");

        CollegeStudent studentTwo = new GradebookCollegeStudent("Ramai","Alexandria",
                "ramai_alexandria@liceokeplero.it");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne,studentTwo));
        when(studentAndGradeService.getGradebook()).thenReturn(collegeStudentList);
        assertIterableEquals(collegeStudentList,studentAndGradeService.getGradebook());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");
    }
    @AfterEach
    public void afterEach () {
        jdbc.execute("DELETE FROM student");
    }
}
