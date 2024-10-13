
# ReviewBot

**ReviewBot** is a powerful web automation tool designed to extract customer reviews from e-commerce platforms such as Shopify, Amazon, and others. This application dynamically identifies CSS elements on a webpage, extracts review data, and processes pagination to retrieve all available reviews. It also integrates an AI model to intelligently select the CSS elements based on the page structure.

## Features

- **Automated Review Extraction**: Scrape product reviews from various platforms.
- **Dynamic CSS Selector Detection**: Uses OpenAI to intelligently identify the correct CSS selectors for review sections.
- **Pagination Handling**: Automatically navigates through multiple pages to collect all reviews.
- **Platform Agnostic**: Supports multiple e-commerce platforms (Shopify, Amazon, etc.).
  
## Tech Stack

- **Java**
- **Spring Boot**
- **Maven**
- **Microsoft Playwright** (for browser automation)
- **OpenAI GPT-3** (for CSS selector identification)
- **MySQL** (for database storage)

## Dependencies

- **Spring Boot Starter Web**: For creating REST APIs.
- **Playwright**: Browser automation framework.
- **OpenAI API**: GPT-3 model for intelligent CSS selector identification.
- **MySQL Driver**: For database connectivity.

## Installation & Setup

### Prerequisites

- Install **Java 21**.
- Install **MySQL Community Server**.
- Install **Maven**.

### Steps

1. Clone the repository:

   ```bash
   git clone <repository_url>
   ```

2. Navigate to the project directory:

   ```bash
   cd reviewbot
   ```

3. Update MySQL credentials in `src/main/resources/application.properties`:

   ```bash
   spring.datasource.username=<your_username>
   spring.datasource.password=<your_password>
   ```

4. Build the project:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Usage

After starting the application, you can interact with the ReviewBot API to start extracting reviews. The following are some key API endpoints:

- **/extract-reviews**: Trigger review extraction for a given product URL.
- **/reviews**: Fetch all extracted reviews from the database.
