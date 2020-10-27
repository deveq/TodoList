# Todo List
#### 개인프로젝트, Kotlin, Android Studio

## 애플리케이션 설명
간단히 할 일을 추가할 수 있는 Todo List 애플리케이션입니다.

## 순서
- 0. 사용한 개념 및 기능
- 1. 완성 화면
- 2. 느낀점

### Room, LiveData, ViewModel
Room, LiveData, ViewModel을 이용한 MVVM패턴을 활용하여
MutableLiveData<MutableList<Todo>>타입으로 받고, Todo객체를 item_list의 View에 바인딩 해줌으로써
화면에 목록으로써 구현됨.
 *Room
  - Entity 생성
  <pre><code>@Entity
data class Todo(var text: String?, var isDone: Boolean = false) : Serializable {
    // 등록하는 순간 System의 시간을 받으므로 등록일 순 정렬에 사용할 기준이 됨.
    @PrimaryKey
    var registerTime : Long = System.currentTimeMillis()

    @ColumnInfo
    var hashTag: String? = null

    // 날짜 기준 검색의 기준이 됨.
    @ColumnInfo
    var time: String? = null
    @ColumnInfo
    var date: String? = null
    @ColumnInfo
    var dateLong: Long? = null
    ...
    ..
    .
</code></pre>
  - Dao 생성
 <pre><code>
 @Dao
interface TodoDao {
    //전체 얻기. 기본값. 등록순서로 보이기
    @Query("select * from Todo order by registerTime desc")
    fun getAll() : MutableList<Todo>

    //전체 얻기. 날짜순으로 얻기.
    @Query("select * from todo order by date, time desc")
    fun getAllTimeOrder() : MutableList<Todo>

    //할일 키워드로 얻기
    @Query("select * from todo where text like '%' ||:text || '%' order by date,time desc")
    fun getTodosByText(text: String) : MutableList<Todo>
    ....
    ...
    ..
    .
</code></pre>

 - Database 생성
 <pre><code>
 @Database(entities = [Todo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
</code></pre>
  
 2. ViewModel
 <pre><code>
 class TodoViewModel(context: Context) : ViewModel() {
    //RoomDatabase 객체를 받아옴.
    private val todoDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "todo")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val todoDao = todoDatabase.todoDao()
    private val todos = todoDao.getAll()
    val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)

    // 정렬 방식 선택 후(등록일 or 날짜) 그 정렬 방식으로 계속 정렬되게끔 하는 flag
    // false : 등록일 기준 정렬, true : 날짜 기준 정렬
    var isTimeOrder: Boolean = false

    fun getList(isTimeOrder: Boolean): MutableList<Todo> {
        return if (isTimeOrder) getAllTimeOrder() else getAll()
    }

    fun getAll() : MutableList<Todo> {
        return todoDao.getAll()
    }

    //전체 얻기. 날짜순으로 얻기.
    fun getAllTimeOrder() : MutableList<Todo> {
        return todoDao.getAllTimeOrder()
    }

    //할일 키워드로 얻기
    fun getTodosByText(text: String) : MutableList<Todo> {
        return todoDao.getTodosByText(text)
    }
    ....
    ...
    ..
    .
</code></pre>

3. LiveData
TodoDao객체에서 todoList 검색 결과를 받은 후 MutableLiveData<List<Todo>>객체 생성 시 인자로 해당 todoList를 넣어주어 생성
  <pre><code>val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)
</code></pre>
 <pre><code>    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //뷰모델 받아오기
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        //recycler view에 보여질 아이템 Room에서 받아오기
        todoList = viewModel.mutableLiveData
        todoList.observe(this, Observer {
            todayAdapter.itemList = it
            todayAdapter.notifyDataSetChanged()
        })

</code></pre>
 
 

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


### 느낀점
- MVVM 


