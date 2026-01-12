/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

//class SavedView : PostFeedViewModel(title = "Saved Posts", repository = SavedRepository()) {
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    override fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?) {
//        val scope = rememberCoroutineScope()
//        TopAppBar(
//            scrollBehavior = scrollBehavior,
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                titleContentColor = MaterialTheme.colorScheme.primary,
//            ),
//            title = {
//                Text("Saved Posts")
//            },
//            navigationIcon = {
//                IconButton(onClick = {
//                    scope.launch { drawerState.open() }
//                }) { Icon(Icons.Default.Menu, "") }
//            },
//            actions = {
//            }
//        )
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SavedViewer(
//    navController: NavController,
//    selected: MutableIntState,
//    viewModel: SavedView = viewModel(),
//    modifier: Modifier = Modifier
//) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
//    Scaffold(
//        topBar = { viewModel.TopBar(rememberDrawerState(DrawerValue.Closed), scrollBehavior) },
//        bottomBar = { BottomBar(selected) },
//        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
//    ) { innerPadding ->
//        PostFeedViewer(
//            viewModel,
//            navController,
//            selected,
//            modifier = Modifier.padding(innerPadding)
//        )
//    }
//}
