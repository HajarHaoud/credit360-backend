# ======================================================================
# CREDIT 360 - BANQUE POPULAIRE
# Script de Validation Automatique des 7 Correctifs d'API
# ======================================================================

$baseUrl = "http://localhost:8080/api"
$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "   TEST DE VALIDATION DES 7 APIS CORRIGEES - CREDIT 360              " -ForegroundColor Yellow
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host ""

$testsPassed = 0
$testsFailed = 0

function Assert-Result($testName, $expectedCode, $actualCode, $details) {
    if ($expectedCode -contains $actualCode) {
        Write-Host "  [SUCCES] $testName" -ForegroundColor Green
        Write-Host "           Status recu: $actualCode (Attendu: $($expectedCode -join ' ou '))" -ForegroundColor Gray
        $script:testsPassed++
    } else {
        Write-Host "  [ECHEC]  $testName" -ForegroundColor Red
        Write-Host "           Status recu: $actualCode (Attendu: $($expectedCode -join ' ou '))" -ForegroundColor Red
        Write-Host "           Details: $details" -ForegroundColor DarkGray
        $script:testsFailed++
    }
}

# --- 0. Verification de la disponibilite du backend ---
Write-Host "[0/7] Verification de l'accessibilite du Backend (Port 8080)..." -ForegroundColor White
try {
    $authPayload = @{ email = "conseiller@credit360.ma"; motDePasse = "demo1234" } | ConvertTo-Json
    $authResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $authPayload -ContentType "application/json" -TimeoutSec 3
    $token = $authResp.token
    Write-Host "      Backend OPERATIONNEL. Token JWT obtenu." -ForegroundColor Green
} catch {
    Write-Host "      [ERREUR] Le backend n'est pas demarre sur http://localhost:8080 !" -ForegroundColor Red
    Write-Host "      Veuillez demarrer le backend via .\run.bat dans une autre fenetre avant de lancer ce script." -ForegroundColor Yellow
    exit 1
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Accept" = "application/json"
}

# Also authenticate as analyst for decision test
$analystToken = $token
try {
    $analystPayload = @{ email = "analyste@credit360.ma"; motDePasse = "demo1234" } | ConvertTo-Json
    $analystResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $analystPayload -ContentType "application/json"
    $analystToken = $analystResp.token
} catch {}

$analystHeaders = @{
    "Authorization" = "Bearer $analystToken"
    "Accept" = "application/json"
}

Write-Host ""
Write-Host "--- EXECUTION DES TESTS DES 7 APIS ---" -ForegroundColor Cyan
Write-Host ""

# ======================================================================
# TEST 1: POST /api/auth/login avec JSON malformé
# ======================================================================
Write-Host "TEST 1 : POST /api/auth/login (Gestion du JSON malforme)" -ForegroundColor Yellow
$malformedJson = '{ "email": "conseiller@credit360.ma", "motDePasse": ' # Syntax error: JSON tronqué
$status1 = 0
$msg1 = ""
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method Post -Body $malformedJson -ContentType "application/json"
    $status1 = $r.StatusCode
} catch {
    $status1 = $_.Exception.Response.StatusCode.value__
    $msg1 = $_.Exception.Message
}
Assert-Result "POST /api/auth/login avec JSON malforme" @(400) $status1 "Avant: 500 Crash | Maintenant: $status1 Bad Request"

# ======================================================================
# TEST 2: POST /api/auth/reset-password avec Token Invalide
# ======================================================================
Write-Host ""
Write-Host "TEST 2 : POST /api/auth/reset-password (Rejet du token invalide)" -ForegroundColor Yellow
$fakeResetPayload = @{
    email = "conseiller@credit360.ma"
    token = "FAUX_TOKEN_PIRATAGE_999"
    nouveauMotDePasse = "NouveauSecret2026!"
} | ConvertTo-Json

$status2 = 0
$msg2 = ""
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/auth/reset-password" -Method Post -Body $fakeResetPayload -ContentType "application/json"
    $status2 = $r.StatusCode
} catch {
    $status2 = $_.Exception.Response.StatusCode.value__
    $msg2 = $_.Exception.Message
}
Assert-Result "POST /api/auth/reset-password avec faux token" @(400, 401) $status2 "Avant: 200 Faille securite | Maintenant: $status2 Rejete"

# ======================================================================
# TEST 3: POST /api/auth/change-password avec Même Ancien Mot de Passe
# ======================================================================
Write-Host ""
Write-Host "TEST 3 : POST /api/auth/change-password (Refus de reutiliser le meme mot de passe)" -ForegroundColor Yellow
$samePassPayload = @{
    ancienMotDePasse = "demo1234"
    nouveauMotDePasse = "demo1234"
    confirmationMotDePasse = "demo1234"
} | ConvertTo-Json

$status3 = 0
$msg3 = ""
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/auth/change-password" -Method Post -Headers $headers -Body $samePassPayload -ContentType "application/json"
    $status3 = $r.StatusCode
} catch {
    $status3 = $_.Exception.Response.StatusCode.value__
    $msg3 = $_.Exception.Message
}
Assert-Result "POST /api/auth/change-password avec meme mot de passe" @(400) $status3 "Avant: 200 Faille validation | Maintenant: $status3 Bad Request"

# ======================================================================
# TEST 4: POST /api/dossiers/{ref}/pieces/{pieceId} avec Fichier Invalide
# ======================================================================
Write-Host ""
Write-Host "TEST 4 : POST /api/dossiers/{ref}/pieces/{pieceId} (Rejet fichier invalide / piece inconnue)" -ForegroundColor Yellow
$status4 = 0
$msg4 = ""
try {
    # Faux format : fichier texte déguisé
    $boundary = [System.Guid]::NewGuid().ToString()
    $body = "--$boundary`r`nContent-Disposition: form-data; name=`"fichier`"; filename=`"virus.exe`"`r`nContent-Type: application/octet-stream`r`n`r`nCeci n'est pas un PDF ni JPG`r`n--$boundary--`r`n"
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00013/pieces/1" -Method Post -Headers $headers -ContentType "multipart/form-data; boundary=$boundary" -Body $body
    $status4 = $r.StatusCode
} catch {
    $status4 = $_.Exception.Response.StatusCode.value__
    $msg4 = $_.Exception.Message
}
Assert-Result "POST piece justificative avec format non autorise" @(400, 404, 409) $status4 "Avant: 500 Crash | Maintenant: $status4 Erreur controlee"

# ======================================================================
# TEST 5: POST /api/dossiers/{ref}/decisions avec Payload Vide {} ou Dossier Non Soumis
# ======================================================================
Write-Host ""
Write-Host "TEST 5 : POST /api/dossiers/{ref}/decisions (Payload vide et dossier non soumis)" -ForegroundColor Yellow

# 5A: Payload vide {}
$status5A = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00014/decisions" -Method Post -Headers $analystHeaders -Body "{}" -ContentType "application/json"
    $status5A = $r.StatusCode
} catch {
    $status5A = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST decision avec payload vide {}" @(400) $status5A "Avant: 500 Crash NPE | Maintenant: $status5A Bad Request"

# 5B: Décision sur dossier A_COMPLETER (CRD00013)
$validDecisionPayload = @{ typeDecision = "VALIDATION"; montantAccorde = 500000; dureeAccordee = 120 } | ConvertTo-Json
$status5B = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00013/decisions" -Method Post -Headers $analystHeaders -Body $validDecisionPayload -ContentType "application/json"
    $status5B = $r.StatusCode
} catch {
    $status5B = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST decision sur dossier non soumis (A_COMPLETER)" @(409) $status5B "Avant: 500 Crash | Maintenant: $status5B Conflit metier"

# ======================================================================
# TEST 6: POST /api/dossiers/{ref}/documents/generer avec Type Invalide
# ======================================================================
Write-Host ""
Write-Host "TEST 6 : POST /api/dossiers/{ref}/documents/generer (Type document vide ou invalide)" -ForegroundColor Yellow
$status6 = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00016/documents/generer" -Method Post -Headers $headers -Body "{}" -ContentType "application/json"
    $status6 = $r.StatusCode
} catch {
    $status6 = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST generer document avec payload vide {}" @(400) $status6 "Avant: 500 Crash NPE | Maintenant: $status6 Bad Request"

# ======================================================================
# TEST 7: POST /api/dossiers/{ref}/signatures/otp sans Paramètre ou Dossier Non Validé
# ======================================================================
Write-Host ""
Write-Host "TEST 7 : POST /api/dossiers/{ref}/signatures/otp (Parametre manquant et validation d'etat)" -ForegroundColor Yellow

# 7A: Paramètre signataire manquant
$status7A = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00016/signatures/otp" -Method Post -Headers $headers
    $status7A = $r.StatusCode
} catch {
    $status7A = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST signatures/otp sans parametre signataire" @(400) $status7A "Avant: 500 Crash | Maintenant: $status7A Bad Request"

# 7B: OTP sur dossier non valide (CRD00013 encore en constitution)
$status7B = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00013/signatures/otp?signataire=CLIENT" -Method Post -Headers $headers
    $status7B = $r.StatusCode
} catch {
    $status7B = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST signatures/otp sur dossier non valide (A_COMPLETER)" @(409) $status7B "Avant: 500 Crash | Maintenant: $status7B Conflit metier"

# 7C: Cas passant nominal sur dossier valide (CRD00016 ACCEPTE)
$status7C = 0
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/dossiers/CRD00016/signatures/otp?signataire=CLIENT" -Method Post -Headers $headers
    $status7C = $r.StatusCode
} catch {
    $status7C = $_.Exception.Response.StatusCode.value__
}
Assert-Result "POST signatures/otp cas nominal (dossier ACCEPTE + signataire CLIENT)" @(200) $status7C "Doit retourner 200 OK avec code OTP"

# ======================================================================
# BILAN DES TESTS
# ======================================================================
Write-Host ""
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "   BILAN DE VALIDATION : $testsPassed SUCCES / $($testsPassed + $testsFailed) TESTS" -ForegroundColor $(if ($testsFailed -eq 0) { "Green" } else { "Red" })
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host ""
