package ru.job4j.accidents.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.Main;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@Transactional
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccidentService accidentService;

    private static final Accident FIRST_TEST_ACCIDENT = new Accident(
            "Дерзкий пролёт утки",
            "Утка пролетела на красный свет низко над дорогой и ей был камерой автоматически выписан штраф",
            "Где-то на трассе",
            new AccidentType(8, "Деревяный человек"));

    @Test
    @WithMockUser
    public void whenTryToGetIndexPageAndGetSuccess() throws Exception {
        this.mockMvc.perform(get("/index"))
                /*.andDo(print())*/ /* Распечатывает html-страницу полностью */
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetIndexPageAnotherWayAndGetSuccess() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetCreateAccidentPageAndGetSuccess() throws Exception {
        this.mockMvc.perform(get("/createAccident"))
                .andExpect(status().isOk())
                .andExpect(view().name("accident/createAccident"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetUpdateAccidentPageAndGetSuccess() throws Exception {
        var accident = FIRST_TEST_ACCIDENT;
        accident = accidentService.add(accident);
        Integer existingId = accident.getId();
        this.mockMvc.perform(get("/formUpdateAccident").param("id", existingId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("accident/editAccident"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetUpdateAccidentPageAndGetErrorPage() throws Exception {
        this.mockMvc.perform(get("/formUpdateAccident").param("id", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetDeleteAccidentPageAndGetSuccess() throws Exception {
        var accident = FIRST_TEST_ACCIDENT;
        accident = accidentService.add(accident);
        Integer existingId = accident.getId();
        this.mockMvc.perform(get("/deleteAccident").param("id", existingId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    @WithMockUser
    public void whenTryToGetDeleteAccidentPageAndGetErrorPage() throws Exception {
        this.mockMvc.perform(get("/deleteAccident").param("id", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }
}