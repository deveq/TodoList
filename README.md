# Todo List
#### 개인프로젝트, Kotlin, Android Studio

## 애플리케이션 설명
간단히 할 일을 추가할 수 있는 Todo List 애플리케이션입니다.

## 순서
- 0. 완성 화면
- 1. 사용한 개념 및 기능
- 2. 동작 설명
- 3. 느낀점

### 완성화면
![ezgif com-video-to-gif](https://user-images.githubusercontent.com/66777885/97251257-daa7ab00-184a-11eb-8ea7-d190ee936d80.gif)

## 주요 기능
### Room , LiveData, ViewModel
Room, LiveData, ViewModel을 이용한 MVVM패턴을 활용하여
MutableLiveData<MutableList<Todo>>타입으로 받고, Todo객체를 item_list의 View에 바인딩 해줌으로써
화면에 목록으로써 구현됨.



### 동작 설명
#### MainActivity
추가 정렬 검색 삭제


#### DetailActivity
일정 상세 설정


