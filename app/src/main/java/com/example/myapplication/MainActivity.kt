package com.example.myapplication

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() { // 앱의 한 화면 액티비티

    private lateinit var binding: ActivityMainBinding // create binding object

    override fun onCreate(savedInstanceState: Bundle?) { // Unity처럼 액티비티 생성시 호출되는 생명주기 메소드.
        super.onCreate(savedInstanceState) // super는 부모 클래스 의미. 부모 클래스의 onCreate()를 실행하겠다는 것.

        binding = ActivityMainBinding.inflate(layoutInflater) // activity_main.xml은 그냥 레이아웃 코드인데 이걸 메모리에 로드하고 객체화. 앞으로 binding 쓰면 됨.
        setContentView(binding.root) // 인플레이트된 레이아웃(binding)의 최상위 뷰(constraintLayout)를 액티비티 화면으로 설정.

        val navView: BottomNavigationView = binding.navView // BottomNavigationView라는 타입의 navView 변수를 선언해 binding.navView 객체를 할당.

        val navController = findNavController(R.id.nav_host_fragment_activity_main) // 메인 액티비티의 화면 전환 등을 가능하게 하는 컨트롤러. id가 nav_host_fragment_activity_main인 뷰를 찾아 NavController를 얻음.
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration( // 최상위 탭은 뒤로가기 안띄워도 되니까 최상위 탭이 뭔지 설정함. 앱 상단의 툴바와 NavController의 화면 전환을 서로 연동하기 위해 필요.
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration) // 앱의 상단 툴바와 navController를 연결해서 뒤로가기 버튼 눌렀을 때 동작을 자동으로 처리하게 하고 각 화면에서 설정한 label이 자동으로 상단 바 제목으로 표시됨.
        navView.setupWithNavController(navController)
    }
}