package com.stopclimatechange.earthgarden.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CheckList {

    public static final Map<Integer, String> checkList = new HashMap<Integer, String>() {{
            put(1, "식기세척기 사용하기");
            put(2, "의약 폐기물은 약국에 배출하기");
            put(3, "개인 용기에 음식 포장하기");
            put(4, "채식하기");
            put(5, "에어컨 또는 보일러 사용하지 않기");
            put(6, "지역 농산물 시장 식품 이용하기");
            put(7, "섬유유연제 대신 식초 소량 넣기");
            put(8, "쌀뜨물로 설거지하기");
            put(9, "고체 샴푸 사용하기");
            put(10, "고체 치약 사용하기");
            put(11, "샤워 시간 10분 이내에 하기");
            put(12, "배달 주문 사용하지 않기");
            put(13, "메일함 비우기");
            put(14, "바닥의 쓰레기 줍기");
            put(15, "사용한 비닐 봉투 재활용하기");
            put(16, "중고 물품 사기");
            put(17, "중고 물품 내놓기");
            put(18, "빨래의 양이 꽉 찼을 때 세탁하기");
            put(19, "일회용컵 대신 개인 컵 사용하기");
            put(20, "음료를 마실 때 빨대 사용하지 않기");
            put(21, "전자 영수증 활용하기");
            put(22, "플라스틱 깨끗이 씻어서 배출하기");
            put(23, "플라스틱 라벨 떼서 배출하기");
            put(24, "필요한 물건만 구매하기");
            put(25, "세수할 때 수도꼭지 잠그기");
            put(26, "양치할 때 컵 사용하기");
            put(27, "가까운 거리 걸어 다니기");
            put(28, "이면지 사용하기");
            put(29, "사용하지 않는 전기코드 뽑기");
            put(30, "장바구니 이용하기");
    }};
}
