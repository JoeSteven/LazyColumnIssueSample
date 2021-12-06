package com.itsmimao.parallax.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val images = listOf(
        "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
        "https://cdn.pixabay.com/photo/2021/07/01/21/20/girl-6380331__480.jpg",
        "https://cdn.pixabay.com/photo/2021/05/25/12/59/mountain-6282389__480.jpg",
        "https://cdn.pixabay.com/photo/2021/07/03/19/56/paris-6384758__480.jpg",
        "https://cdn.pixabay.com/photo/2021/05/14/10/00/flowers-6253005__480.jpg",
        "https://cdn.pixabay.com/photo/2014/10/25/19/24/rape-blossom-502973__480.jpg",
        "https://cdn.pixabay.com/photo/2021/06/02/20/56/antelope-canyon-6305458__480.jpg",
        "https://cdn.pixabay.com/photo/2020/10/25/09/23/seagull-5683637__480.jpg",
        "https://cdn.pixabay.com/photo/2017/06/05/07/59/octopus-2373177__480.png",
        "https://cdn.pixabay.com/photo/2020/10/05/07/29/woman-5628426__480.jpg"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyListSample(generateList(200))
        }
    }

    private fun generateList(count: Int): List<ListData> {
        val list = mutableListOf<ListData>()
        for (i in 0 until  count) {
            list.add(
                ListData(
                    id = UUID.randomUUID().toString(),
                    name = "Items $i",
                    avatar = "https://placekitten.com/200/300",
                    content = "Content Text $i",
                    media = images[Random.nextInt(images.size)]
                )
            )
        }
        return list
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LazyListSample(datas:List<ListData>) {
    val listState = rememberLazyListState()
    val firstIndex = remember{
        mutableStateOf(0)
    }
    var lastIndex = 0
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                firstIndex.value = it
                // when scroll content from bottom to top, the first visible index suppose to be smaller
                if (firstIndex.value > lastIndex) Log.e("ScrollToTop", "wrong position! expect:${lastIndex - 1} but current is:$it ")
                // when scroll content from top to bottom, the first visible index suppose to be bigger
                if (firstIndex.value < lastIndex) Log.e("ScrollToBottom", "wrong position! expect:${lastIndex + 1} but current is:$it ")
                lastIndex = firstIndex.value
            }
    }
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            items(
                items = datas,
                key = {
                    it.id
                }
            ) { item ->
                ListItem {
                    Column {
                        Spacer(modifier = Modifier.width(10.dp))
                        Row {
                            Image(
                                modifier = Modifier.size(48.dp),
                                painter = rememberCoilPainter(request = item.avatar),
                                contentDescription = "avatar")
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = item.name)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = item.content)
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = rememberCoilPainter(request = item.media),
                            contentDescription = "media"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Divider()
                    }
                }
            }
        }
        Text(text = "${firstIndex.value}")
    }

}

data class ListData(
    val id: String,
    val name: String,
    val avatar: String,
    val content: String,
    val media: String,
)

fun test(){

}