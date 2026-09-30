package com.example.data.model

data class VideoChapter(
    val timeCode: String,
    val title: String,
    val description: String
)

data class VideoTutorial(
    val id: String,
    val title: String,
    val chefName: String,
    val durationText: String,
    val youtubeUrl: String,
    val overview: String,
    val chapters: List<VideoChapter>,
    val secretsOfChef: List<String>
)
