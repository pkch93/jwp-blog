package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleInputException;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(final ArticleService articleService, final CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView createArticle(@Valid ArticleDto articleDto, Errors errors, User user) {
        if (errors.hasErrors()) {
            throw new ArticleInputException("입력값이 잘못되었습니다.");
        }

        articleDto.setAuthor(user);
        Article article = articleService.create(articleDto);

        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);

        List<Comment> comments = commentService.findAllByArticleId(articleId);
        model.addAttribute("comments", comments);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditPage(@PathVariable Long articleId, Model model, User user) {
        Article article = articleService.findById(articleId, user);
        model.addAttribute("article", article);

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable Long articleId, ArticleDto articleDto, User user) {
        Article updateArticle = articleService.update(articleDto, articleId, user);

        return new RedirectView("/articles/" + updateArticle.getId());
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId, User user) {
        articleService.delete(articleId, user);
        return new RedirectView("/");
    }

    @ExceptionHandler({NotMatchAuthenticationException.class, ArticleNotFoundException.class, ArticleInputException.class})
    public RedirectView articleException(Exception exception) {
        return new RedirectView("/");
    }
}