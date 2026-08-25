#!/usr/bin/env bash
# ==================================================================
#  upload-to-github.sh
#  Otomatisasi upload project ke repositori GitHub
#  Author: BAGAS RAMANDANI
# ==================================================================
set -e

# ================== KONFIGURASI (edit sesuai kebutuhan) ==================
GIT_USERNAME="BAGAS RAMANDANI"
GIT_EMAIL="bagasramandani@example.com"     # ganti dengan email GitHub kamu
REPO_URL=""                                 # contoh: https://github.com/USERNAME/REPO-NAME.git
BRANCH="main"
COMMIT_MESSAGE="Update project"
# ===========================================================================

echo "=================================================="
echo " Upload ke GitHub - by $GIT_USERNAME"
echo "=================================================="

# --- Validasi: pastikan git terpasang ---
if ! command -v git >/dev/null 2>&1; then
  echo ">> git belum terpasang, menginstall..."
  pkg install git -y 2>/dev/null || apt install git -y
fi

# --- Validasi: pastikan REPO_URL sudah diisi ---
if [ -z "$REPO_URL" ]; then
  read -rp "Masukkan URL repo GitHub (contoh: https://github.com/USERNAME/REPO.git): " REPO_URL
fi

if [ -z "$REPO_URL" ]; then
  echo "Error: URL repo tidak boleh kosong."
  exit 1
fi

# --- Set identitas git (author) ---
echo ">> Set author git: $GIT_USERNAME <$GIT_EMAIL>"
git config --global user.name "$GIT_USERNAME"
git config --global user.email "$GIT_EMAIL"

# --- Inisialisasi repo jika belum ada ---
if [ ! -d ".git" ]; then
  echo ">> Inisialisasi repositori git baru"
  git init
  git branch -M "$BRANCH"
fi

# --- Tambahkan remote jika belum ada ---
if ! git remote get-url origin >/dev/null 2>&1; then
  echo ">> Menambahkan remote origin: $REPO_URL"
  git remote add origin "$REPO_URL"
else
  echo ">> Remote origin sudah ada, memperbarui URL"
  git remote set-url origin "$REPO_URL"
fi

# --- Buat/update file AUTHORS ---
if [ ! -f "AUTHORS.md" ]; then
  echo ">> Membuat file AUTHORS.md"
  cat > AUTHORS.md <<EOF
# Authors

- **BAGAS RAMANDANI** - Author & Maintainer
EOF
fi

# --- Staging semua perubahan ---
echo ">> Menambahkan semua file ke staging"
git add .

# --- Cek apakah ada perubahan untuk di-commit ---
if git diff --cached --quiet; then
  echo ">> Tidak ada perubahan baru untuk di-commit."
else
  echo ">> Commit perubahan"
  read -rp "Commit message [default: $COMMIT_MESSAGE]: " CUSTOM_MSG
  FINAL_MSG="${CUSTOM_MSG:-$COMMIT_MESSAGE}"
  git commit -m "$FINAL_MSG" --author="$GIT_USERNAME <$GIT_EMAIL>"
fi

# --- Push ke GitHub ---
echo ">> Push ke $REPO_URL ($BRANCH)"
echo ">> Kamu mungkin akan diminta login GitHub (username + Personal Access Token, bukan password akun)"
git push -u origin "$BRANCH"

echo ""
echo "=================================================="
echo " Selesai! Project berhasil di-push ke GitHub."
echo " Author: $GIT_USERNAME"
echo "=================================================="
