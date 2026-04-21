# Japanese (Keigo) Translator for PR

Automatically generates professional Japanese (Keigo) summaries for your Pull Requests using Google Gemini AI 3.

## 📷 Example Output
### PR In English

<img width="1294" height="815" alt="Screenshot 2026-04-21 125102" src="https://github.com/user-attachments/assets/fa9cc54b-c5d6-4c01-87cc-985d497a74fd" />

#### Japanese translation 
<img width="1083" height="740" alt="image" src="https://github.com/user-attachments/assets/229cfc64-8a02-4c08-9ce8-21caf6a3a111" />

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




