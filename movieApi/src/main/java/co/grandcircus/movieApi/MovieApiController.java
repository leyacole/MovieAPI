package co.grandcircus.movieApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.grandcircus.movieApi.dao.MovieApiDao;
import co.grandcircus.movieApi.entity.Movie;

@RestController
public class MovieApiController {

	@Autowired
	private MovieApiDao dao;

	@GetMapping("/")
	public ModelAndView redirect() {
		return new ModelAndView("redirect:/movies");

	}

	@GetMapping("/movie")
	public List<Movie> listMovie(@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "title", required = false) String title) {

		if (category == null || category.isEmpty() && title == null || title.isEmpty()) {
			return dao.findAll();

		} else if (title == null || title.isEmpty()) {
			return dao.findByCategory(category);
		}

		else {
			return dao.findByTitleContainsIgnoreCase(title);
		}
	}

	@GetMapping("/movie/random")
	public Movie randomMovie(@RequestParam(value = "category", required = true) String category) {

		if (category == null || category.isEmpty()) {
			Long rand = (long) (Math.random() * 10) + 1;
			return dao.findById(rand).get();

		} else {
			List<Movie> catlist = dao.findByCategory(category);
			int rand = (int) (Math.random() * catlist.size()) + 1;
			return catlist.get(rand - 1);

		}

	}

	@GetMapping("/movies/random-list")
	public List<Movie> listrandom(@RequestParam(value = "quantity", required = true) Integer quantity) {
		List<Movie> movielist = dao.findAll();
		List<Movie> randlist = new ArrayList<Movie>();
		Collections.shuffle(movielist);
		for (int i = 0; i < quantity; i++) {
			randlist.add(movielist.get(i));
		}
		return randlist;
	}

	@GetMapping("/categories")
	public List<String> getAllCategories() {
		List<Movie> movielist = dao.findAll();
		List<String> movieCategories = new ArrayList<String>();
		for (Movie movie : movielist) {
			if (!movieCategories.contains(movie.getCategory())) {
				movieCategories.add(movie.getCategory());
			}
		}
		return movieCategories;
	}

	@GetMapping("/movie/{id}")
	public Movie listMovie(@PathVariable("id") Long id) {
		return dao.findById(id).get();
	}

}
