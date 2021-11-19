package com.navi.file.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.navi.file.adapter.MyFileFragmentAdapter
import com.navi.file.databinding.MyFileRecyclerViewBinding
import com.navi.file.model.FileData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFileFragment @Inject constructor() : Fragment() {
    private var _binding : MyFileRecyclerViewBinding?= null
    private val myFileRecyclerViewBinding : MyFileRecyclerViewBinding get() = _binding!!
    lateinit var recyclerview : RecyclerView
    lateinit var RecyclerViewAdapter : MyFileFragmentAdapter

    private val list = arrayListOf<FileData>(
        FileData("웹프로그래밍","2020년08월09일에 수정함",false),
        FileData("자료구조","2020년01월02일에 수정함",false),
        FileData("컴퓨터구조","2020년04월16일에 수정함",false),
        FileData("테크니컬 영어 말하기","2020년06월17일에 수정함",true),
        FileData("튜터","2020년07월31일에 수정함",false),
        FileData("전기프1 캘린더","2020년08월09일에 수정함",true),
        FileData("컴퓨테이션이론","2020년02월05일에 수정함",false),
        FileData("정보보안","2020년03월08일에 수정함",false),
        FileData("졸업프로젝트","2020년04월09일에 수정함",true),
        FileData("C프로그래밍 테트리스","2020년08월21일에 수정함",false),
        FileData("오늘 할 일","2020년12월24일에 수정함",true),
        FileData("교양","2020년12월30일에 수정함",true),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyFileRecyclerViewBinding.inflate(layoutInflater, container, false)
        return myFileRecyclerViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview = myFileRecyclerViewBinding.naviMainRecycler
        recyclerview.layoutManager = LinearLayoutManager(context)

        RecyclerViewAdapter = MyFileFragmentAdapter(list)
        recyclerview.adapter = RecyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}