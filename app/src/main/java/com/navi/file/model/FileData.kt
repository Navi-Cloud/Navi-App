package com.navi.file.model

data class FileData (
    var filename : String,

    //숫자로 하는 경우 -> 정렬할 때 간편 / 화면에 출력할 때 파싱해야 함
    //문자열로 하는 경우 -> 정렬할때 ... / 화면에 그대로 출력
    var date : String,

    var isFolder : Boolean
)