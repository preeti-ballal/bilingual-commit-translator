# Japanese (Keigo) Translator for PR

Automatically generates professional Japanese (Keigo) summaries for your Pull Requests using Google Gemini AI.

## 🚀 Features
- **Automated Summaries:** No more manual translations.
- **Professional Keigo:** Tailored for Japanese corporate environments.
- **Gemini Powered:** Uses state-of-the-art AI for context-aware translation.

## 🛠 Setup

### 1. Get a Gemini API Key
Obtain an API key from [Google AI Studio](https://aistudio.google.com/).

### 2. Add Secrets
In your GitHub Repository, go to **Settings > Secrets and variables > Actions** and add:
- `GEMINI_API_KEY`: Your Google Gemini API Key.

### 3. Create Workflow
Create `.github/workflows/translate.yml` in your repo:

```yaml
on:
  pull_request:
    types: [opened, edited, synchronize]

jobs:
  translate:
    runs-on: ubuntu-latest
    permissions:
      pull-requests: write
    steps:
      - uses: your-username/bilingual-commit-translator@main
        with:
          gemini_api_key: ${{ secrets.GEMINI_API_KEY }}
          github_token: ${{ secrets.GITHUB_TOKEN }}
