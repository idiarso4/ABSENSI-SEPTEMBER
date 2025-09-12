param(
  [string]$Root = "backend/src/main/java"
)

Write-Host "Scanning *.java under $Root for replacements..."

$files = Get-ChildItem -Recurse -File $Root -Include *.java
$total = 0
foreach ($f in $files) {
  $c = Get-Content -Raw -LiteralPath $f.FullName
  $n = $c
  # Package and import renames from com.school.sim -> com.simsekolah
  $n = $n -replace 'package\s+com\.school\.sim','package com.simsekolah'
  $n = $n -replace 'import\s+com\.school\.sim','import com.simsekolah'
  # Global FQN usages
  $n = $n -replace 'com\.school\.sim','com.simsekolah'
  # javax -> jakarta for Spring Boot 3
  $n = $n -replace 'javax\.persistence','jakarta.persistence'
  $n = $n -replace 'javax\.validation','jakarta.validation'
  $n = $n -replace 'javax\.servlet','jakarta.servlet'

  if ($n -ne $c) {
    [System.IO.File]::WriteAllText($f.FullName, $n)
    $total++
  }
}

Write-Host "Updated files:" $total
