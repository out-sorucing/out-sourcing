package com.sparta.outsorucing.domain.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.outsorucing.common.config.ImageUtil;
import com.sparta.outsorucing.common.enums.Status;
import com.sparta.outsorucing.common.exception.InvalidRequestException;
import com.sparta.outsorucing.domain.member.entity.Member;
import com.sparta.outsorucing.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.dto.MenuResponseDto;
import com.sparta.outsorucing.domain.menu.dto.UpdateMenuRequestDto;
import com.sparta.outsorucing.domain.menu.entity.Menu;
import com.sparta.outsorucing.domain.menu.repository.MenuRepository;
import com.sparta.outsorucing.domain.store.entity.Store;
import com.sparta.outsorucing.domain.store.repository.StoreRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ImageUtil imageUtil;
    @InjectMocks
    private MenuService menuService;


    @Test
    @DisplayName("메뉴 생성 - 메뉴 추가 성공 테스트")
    void createTest1() throws Exception {
        //given
        Store store = new Store();
        Member member = new Member();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        Menu menu = Menu.builder().menuName("createMenuRequest").price(1234).content("createMe").status(Status.ACTIVE).store(store).build();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(menu,
                                     "imageUri",
                                     "spring.png");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.save(any())).thenReturn(menu);

        //when
        MenuResponseDto create = menuService.createMenu(1L,
                                                        createMenuRequestDto,
                                                        multipartFile,
                                                        1L);
        //then
        assertEquals(create.getMenuName(),
                     "createMenuRequest");
        assertEquals(create.getImageUri(),
                     ("spring.png"));
        verify(menuRepository,
               times(1)).save(any());
    }

    @Test
    @DisplayName("메뉴 생성 - 존재하지 않는 가게일 시 예외처리")
    void createTest2() throws IOException {
        //given
        Member member = new Member();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.createMenu(1L,
                                                                               createMenuRequestDto,
                                                                               multipartFile,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "존재하지 않는 가게입니다.");
        verify(menuRepository,
               times(0)).save(any());
    }

    @Test
    @DisplayName("메뉴 생성 - 본인의 가게가 아닐 시 예외처리")
    void createTest3() throws IOException {
        //given
        Store store = new Store();
        Member member = new Member();
        Member member1 = new Member();
        CreateMenuRequestDto createMenuRequestDto = new CreateMenuRequestDto();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        ReflectionTestUtils.setField(store,
                                     "id",
                                     2L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member1);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     2L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.createMenu(1L,
                                                                               createMenuRequestDto,
                                                                               multipartFile,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "본인 가게가 아닙니다.");
        verify(menuRepository,
               times(0)).save(any());
    }

    @Test
    @DisplayName("메뉴 수정 - 본인의 가게가 아닐 시 예외처리")
    void updateTest1() throws IOException{
        //given
        Store store = new Store();
        Member member = new Member();
        Member member1 = new Member();
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        ReflectionTestUtils.setField(store,
                                     "id",
                                     2L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member1);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     2L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.UpdateMenu(1L,
                                                                               1L,
                                                                               updateMenuRequestDto,
                                                                               multipartFile,
                                                                               member.getId()));
        //then
        assertEquals(ex.getMessage(),
                     "본인 가게가 아닙니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 삭제된 메뉴 수정 요청 시 예외처리")
    void updateTest2() throws IOException{
        //given
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder().menuName("createMenuRequest").price(1234).content("createMe").status(Status.DELETE).store(store).build();
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(any(),
                                               any())).thenReturn(Optional.of(menu));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.UpdateMenu(1L,
                                                                               1L,
                                                                               updateMenuRequestDto,
                                                                               multipartFile,
                                                                               member.getId()));
        //then
        assertEquals(ex.getMessage(),
                     "삭제된 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 존재하지 않는 가게일 시 예외처리")
    void updateTest3() throws IOException {
        //given
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.UpdateMenu(1L,
                                                                               1L,
                                                                               updateMenuRequestDto,
                                                                               multipartFile,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "존재하지 않는 가게입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 가게에 존재하지 않는 메뉴 수정 요청 시 예외처리")
    void updateTest4() throws IOException {
        //given
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        Store store = new Store();
        Member member = new Member();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,
                                               1L)).thenReturn(Optional.empty());

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.UpdateMenu(1L,
                                                                               1L,
                                                                               updateMenuRequestDto,
                                                                               multipartFile,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "가게에 존재하지 않는 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 수정 - 수정 성공")
    void updateTest5() throws IOException{
        //given
        UpdateMenuRequestDto updateMenuRequestDto = new UpdateMenuRequestDto();
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder().menuName("createMenuRequest").price(1234).content("createMe").status(Status.ACTIVE).store(store).build();
        MultipartFile multipartFile = new MockMultipartFile("images",
                                                            "spring.png",
                                                            // 파일 이름
                                                            "image/png",
                                                            // 파일의 확장자 타입
                                                            new FileInputStream(new File("C:/Users/dlgkt/Downloads/Untitled.png"))); // 실제 자기 이미지 파일 경로 아무거나
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(menu,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(updateMenuRequestDto,
                                     "menuName",
                                     "update");
        ReflectionTestUtils.setField(updateMenuRequestDto,
                                     "price",
                                     20000);
        ReflectionTestUtils.setField(updateMenuRequestDto,
                                     "content",
                                     "upupd");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,
                                               1L)).thenReturn(Optional.of(menu));

        //when
        MenuResponseDto menuResponseDto = menuService.UpdateMenu(1L,
                                                                 1L,
                                                                 updateMenuRequestDto,
                                                                 multipartFile,
                                                                 1L);
        //then
        assertEquals(menuResponseDto.getMenuName(),
                     "update");
        assertEquals(menuResponseDto.getPrice(),
                     20000);
        assertEquals(menuResponseDto.getContent(),
                     "upupd");
    }

    @Test
    @DisplayName("메뉴 삭제 - 본인의 가게가 아닐 시 예외처리")
    void deleteTest1() {
        //given
        Store store = new Store();
        Member member = new Member();
        ReflectionTestUtils.setField(store,
                                     "id",
                                     2L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     2L);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.deleteMenu(1L,
                                                                               1L,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "본인 가게가 아닙니다.");
    }

    @Test
    @DisplayName("메뉴 삭제 - 존재하지 않는 가게 일 경우 예외처리")
    void deleteTest2() {
        //given
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.deleteMenu(1L,
                                                                               1L,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "존재하지 않는 가게입니다.");
    }

    @Test
    @DisplayName("메뉴 삭제 - 가게에 존재하지 않는 메뉴일 경우 예외처리")
    void deleteTest3() {
        //given
        Store store = new Store();
        Member member = new Member();
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,
                                               1L)).thenReturn(Optional.empty());

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.deleteMenu(1L,
                                                                               1L,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "가게에 존재하지 않는 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 삭제 - 이미 삭제된 메뉴일 경우 예외처리")
    void deleteTest4() {
        //given
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder().menuName("createMenuRequest").price(1234).content("createMe").status(Status.DELETE).store(store).build();
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,
                                               1L)).thenReturn(Optional.of(menu));

        //when
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                                                  () -> menuService.deleteMenu(1L,
                                                                               1L,
                                                                               1L));
        //then
        assertEquals(ex.getMessage(),
                     "이미 삭제된 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 삭제 - 삭제 성공")
    void deleteTest5() {
        //given
        Store store = new Store();
        Member member = new Member();
        Menu menu = Menu.builder().menuName("createMenuRequest").price(1234).content("createMe").status(Status.ACTIVE).store(store).build();
        ReflectionTestUtils.setField(store,
                                     "id",
                                     1L);
        ReflectionTestUtils.setField(store,
                                     "member",
                                     member);
        ReflectionTestUtils.setField(member,
                                     "id",
                                     1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndStoreId(1L,
                                               1L)).thenReturn(Optional.of(menu));

        //when
        String menuName = menuService.deleteMenu(1L,
                                                 1L,
                                                 1L);

        //then
        assertEquals(menuName,
                     "createMenuRequest");
    }
}
