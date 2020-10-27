# Todo List
#### 개인프로젝트, Kotlin, Android Studio

## 애플리케이션 설명
간단히 할 일을 추가할 수 있는 Todo List 애플리케이션입니다.

## 순서
- 0. 사용한 개념 및 기능
- 1. 완성 화면
- 2. 느낀점

### 완성화면
#### 할 일 추가
![add_todo](https://user-images.githubusercontent.com/66777885/97325733-a2d64d00-18b6-11eb-97e4-dff04270d94b.gif)

#### 상세보기 (상세설정)
![detail](https://user-images.githubusercontent.com/66777885/97325823-b8e40d80-18b6-11eb-8102-ebd4c3b6c95d.gif)

#### 완료하기
![done](https://user-images.githubusercontent.com/66777885/97325869-c4373900-18b6-11eb-9b2f-de512f59cc03.gif)

#### 삭제하기
![delete](https://user-images.githubusercontent.com/66777885/97325903-cc8f7400-18b6-11eb-9936-d98a1f8cdded.gif)

#### 검색하기
![search](https://user-images.githubusercontent.com/66777885/97325931-d44f1880-18b6-11eb-8fd8-8d9f2b695433.gif)

#### 정렬하기
![sort](https://user-images.githubusercontent.com/66777885/97325967-dadd9000-18b6-11eb-832b-a2060169c45d.gif)


### Room , LiveData, ViewModel
Room, LiveData, ViewModel을 이용한 MVVM패턴을 활용하여
MutableLiveData<MutableList<Todo>>타입으로 받고, Todo객체를 item_list의 View에 바인딩 해줌으로써
화면에 목록으로써 구현됨.


#### DetailActivity
일정 상세 설정


