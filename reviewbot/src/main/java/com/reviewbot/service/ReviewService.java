package com.reviewbot.service;

import com.microsoft.playwright.*;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ReviewService {
    Logger logger = Logger.getLogger(ReviewService.class.getName());

    @Value("${openai.api.key}")
    private String openaiApiKey;

    // Extract reviews from the given page URL
    public Map<String, Object> extractReviews(String pageUrl) {
        logger.info("Extracting reviews from: " + pageUrl);
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> reviews = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            // Launch the browser
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(pageUrl);

            // Fetch the HTML content of the page
            String htmlContent = page.content();

            // Send the HTML content to OpenAI to identify CSS selectors
            String cssSelectorsJson = identifyCss(htmlContent);

            // Simulated CSS selectors based on LLM response (parse from OpenAI)
            String reviewSelector = ".review";  // Example CSS selector
            String titleSelector = ".review-title";
            String bodySelector = ".review-body";
            String ratingSelector = ".review-rating";
            String reviewerSelector = ".reviewer-name";

            // Extract reviews from the current page
            extractReviewsFromPage(page, reviewSelector, titleSelector, bodySelector, ratingSelector, reviewerSelector, reviews);

            // Handle pagination and extract reviews from all pages
            handlePagination(page, reviewSelector, titleSelector, bodySelector, ratingSelector, reviewerSelector, reviews);

            // Add reviews and count to response
            response.put("reviews_count", reviews.size());
            response.put("reviews", reviews);

            // Close the browser
            browser.close();
        } catch (Exception e) {
            logger.severe("Error extracting reviews: " + e.getMessage());
            e.printStackTrace();
        }
        logger.info("Extracted " + reviews.size() + " reviews");
        return response;
    }

    // Method to extract reviews from a page
    private void extractReviewsFromPage(Page page, String reviewSelector, String titleSelector, String bodySelector, String ratingSelector, String reviewerSelector, List<Map<String, String>> reviews) {
        logger.info("Extracting reviews from page");
        List<ElementHandle> reviewElements = page.querySelectorAll(reviewSelector);

        for (ElementHandle reviewElement : reviewElements) {
            Map<String, String> review = new HashMap<>();
            review.put("title", reviewElement.querySelector(titleSelector).innerText());
            review.put("body", reviewElement.querySelector(bodySelector).innerText());
            review.put("rating", reviewElement.querySelector(ratingSelector).innerText());
            review.put("reviewer", reviewElement.querySelector(reviewerSelector).innerText());
            logger.info("Extracted review: " + review);
            reviews.add(review);
        }
    }

    // Method to handle pagination and extract reviews from all pages
    private void handlePagination(Page page, String reviewSelector, String titleSelector, String bodySelector, String ratingSelector, String reviewerSelector, List<Map<String, String>> reviews) {
        logger.info("Handling pagination");
        while (page.querySelector("button.next") != null) {
            page.click("button.next");
            page.waitForLoadState();

            logger.info("Extracting reviews from next page");
            // Extract reviews from the next page
            extractReviewsFromPage(page, reviewSelector, titleSelector, bodySelector, ratingSelector, reviewerSelector, reviews);
        }
    }

    // OpenAI Integration for identifying CSS selectors dynamically
    private String identifyCss(String htmlContent) {
        logger.info("Identifying CSS selectors for reviews");
        OpenAiService service = new OpenAiService(openaiApiKey);
        CompletionRequest request = CompletionRequest.builder()
            .prompt("Identify CSS selectors for reviews in the following HTML: " + htmlContent)
            .model("gpt-4")
            .maxTokens(200)
            .build();

        logger.info("Sending request to OpenAI for identifying CSS selectors");
        return service.createCompletion(request).getChoices().get(0).getText();
    }
}
