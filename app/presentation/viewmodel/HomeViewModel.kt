class HomeViewModel(private val repository: TourRepository) : ViewModel() {

    var tourList by mutableStateOf<List<TourModel>>(emptyList())
        private set

    init {
        loadTours()
    }

    private fun loadTours() {
        repository.fetchTours { tours ->
            tourList = tours
        }
    }
}
