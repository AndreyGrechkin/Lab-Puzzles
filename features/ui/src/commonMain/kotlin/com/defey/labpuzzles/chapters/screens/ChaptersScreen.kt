package com.defey.labpuzzles.chapters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.chapters.ChaptersUiContract
import com.defey.labpuzzles.chapters.ChaptersViewModel
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_chapter_9
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChaptersScreen() {
    val viewModel: ChaptersViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_chapter_9),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(paddingValues)) {
                ChaptersSelectTopBar(onBackClick = {
                    viewModel.onEvent(ChaptersUiContract.Event.OnBack)
                }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.chapters) { chapterItem ->
                        ChaptersListItem(
                            chapterItem = chapterItem,
                            onChapterSelect = {
                                viewModel.onEvent(ChaptersUiContract.Event.OnChapterSelect(it))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        )
                    }
                }
            }
        }
    }
}