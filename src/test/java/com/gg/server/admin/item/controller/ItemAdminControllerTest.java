package com.gg.server.admin.item.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.server.admin.item.data.ItemAdminRepository;
import com.gg.server.admin.item.dto.ItemListResponseDto;
import com.gg.server.admin.item.dto.ItemUpdateRequestDto;
import com.gg.server.admin.item.service.ItemAdminService;
import com.gg.server.domain.item.data.Item;
import com.gg.server.domain.item.type.ItemType;
import com.gg.server.domain.user.data.UserRepository;
import com.gg.server.global.security.jwt.utils.AuthTokenProvider;
import com.gg.server.utils.ItemTestUtils;
import com.gg.server.utils.TestDataUtils;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemAdminControllerTest {
    @Autowired
    ItemAdminService itemAdminService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TestDataUtils testDataUtils;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthTokenProvider tokenProvider;
    @Autowired
    ItemAdminRepository itemAdminRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ItemTestUtils itemTestUtils;

    Item item;
    @BeforeEach
    void setUp() {
        ItemUpdateRequestDto dto = new ItemUpdateRequestDto("name", "content",
            "subContent", 100, 50, ItemType.EDGE);
        item = itemTestUtils.createItem(testDataUtils.createAdminUserForItem(), dto);
    }

    @Test
    @DisplayName("GET /pingpong/admin/items/history")
    public void getAllItemHistoryTest() throws Exception {
        String accessToken = testDataUtils.getAdminLoginAccessToken();
        Integer page = 1;
        Integer size = 20;
        String url = "/pingpong/admin/items/history?page=" + page + "&size=" + size;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        String contentAsString = mockMvc.perform(get(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(contentAsString);
        ItemListResponseDto expect = itemAdminService.getAllItemHistory(pageable);
        System.out.println(expect.getHistoryList());
        ItemListResponseDto result = objectMapper.readValue(contentAsString, ItemListResponseDto.class);
        System.out.println(expect.getHistoryList().get(0));
        System.out.println(result.getHistoryList().get(0));
        assertThat(result.getHistoryList().get(0).getItemId());
        assertThat(result.getHistoryList().get(0).getName());
        assertThat(result.getHistoryList().get(0).getMainContent());
        assertThat(result.getHistoryList().get(0).getSubContent());
        assertThat(result.getHistoryList().get(0).getPrice());
    }

//    @Test
//    @DisplayName("POST /pingpong/admin/items/history/{itemId}")
//    public void updateItemTest() throws Exception {
//        String accessToken = testDataUtils.getAdminLoginAccessToken();
//        Long userId = tokenProvider.getUserIdFromAccessToken(accessToken);
//        String creatorId = userRepository.getById(userId).getIntraId();
//        MockMultipartFile image = new MockMultipartFile("file", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
//        MockMultipartFile jsonFile = new MockMultipartFile("itemRequestDto", "",
//            "application/json",
//            ("{\"name\": \"TEST\", "
//                + "\"mainContent\": \"TESTING\", "
//                + "\"subContent\": \"TESTING\", "
//                + "\"price\": 42, "
//                + "\"discount\": 50, "
//                + "\"itemType\": \"MEGAPHONE\"}").getBytes());
//        String contentAsString = mockMvc.perform(multipart("/pingpong/admin/items/{itemId}", item.getId())
////                .file(image)
//                .file(jsonFile)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
//                .andExpect(status().isNoContent())
//                .andReturn().getResponse().getContentAsString();
//    }

    @Test
    @DisplayName("DELETE /pingpong/admin/items/{itemId}")
    public void deleteItemTest() throws Exception {
        String accessToken = testDataUtils.getAdminLoginAccessToken();
        Long userId = tokenProvider.getUserIdFromAccessToken(accessToken);
        String deleterId = userRepository.getById(userId).getIntraId();
        String contentAsString = mockMvc.perform(delete("/pingpong/admin/items/{itemId}", item.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();
        List<Item> list = itemAdminRepository.findAll();
        assertThat(list.get(0).getDeleterIntraId()).isEqualTo(deleterId);
    }
}