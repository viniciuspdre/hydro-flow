# -*- coding: utf-8 -*-
"""Gera casos-de-teste.pdf a partir de casos-de-teste.md com formatação ABNT."""
import os, re
from fpdf import FPDF

DIR = os.path.dirname(os.path.abspath(__file__))
MD  = os.path.join(DIR, "casos-de-teste.md")
PDF = os.path.join(DIR, "casos-de-teste.pdf")
LH  = 5.5  # line-height 1.5 × 12pt

class Doc(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font("ARIAL", "", 10)
            self.set_y(15)
            self.cell(0, 10, str(self.page_no()), align="R", new_x="RIGHT", new_y="TOP")
            self.set_y(30)
    def footer(self): pass

pdf = Doc("P", "mm", "A4")
pdf.add_font("ARIAL", "",  r"C:\Windows\Fonts\arial.ttf")
pdf.add_font("ARIAL", "B", r"C:\Windows\Fonts\arialbd.ttf")
pdf.add_font("ARIAL", "I", r"C:\Windows\Fonts\ariali.ttf")
pdf.set_margins(30, 30, 20)
pdf.set_auto_page_break(True, 20)
W = 160  # printable width = 210-30-20

# ── CAPA ──
pdf.add_page()
pdf.set_text_color(0, 0, 0)
pdf.set_font("ARIAL", "", 12)
pdf.set_y(30)
pdf.cell(W, LH, "INSTITUTO FEDERAL DE PERNAMBUCO CAMPUS BELO JARDIM", align="C", new_x="LMARGIN", new_y="NEXT")

pdf.set_y(75)
for m in ["CAMILLE MARIA DIAS DE BARROS SILVA","GABRIEL CAMELO","IGOR NAYAN",
          "LUCAS CAVALCANTE DA SILVA","PEDRO VINICIUS SILVA LIRA"]:
    pdf.cell(W, LH, m, align="C", new_x="LMARGIN", new_y="NEXT")

pdf.set_y(140)
pdf.set_font("ARIAL", "B", 12)
pdf.multi_cell(W, LH, "Documentação de Casos de Teste\nMais água para nosso povo!", align="C")

pdf.set_y(250)
pdf.set_font("ARIAL", "", 12)
pdf.cell(W, LH, "BELO JARDIM, PE", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.cell(W, LH, "2026", align="C", new_x="LMARGIN", new_y="NEXT")

# ── CONTEÚDO ──
with open(MD, "r", encoding="utf-8") as f:
    lines = f.readlines()

pdf.add_page()
started = False
INDENT_FIELD = 0   # recuo do ● em relação à margem esquerda
INDENT_LIST  = 10  # recuo das listas numeradas

for raw in lines:
    line = raw.rstrip("\r\n").strip()

    if line.startswith("Módulo de"):
        started = True

    if not started:
        continue

    # linhas vazias / separadores
    if line == "" or line.startswith("<div"):
        pdf.ln(3)
        continue

    # Título de módulo
    if line.startswith("Módulo de"):
        if pdf.get_y() > pdf.page_break_trigger - 40:
            pdf.add_page()
        pdf.ln(6)
        pdf.set_font("ARIAL", "B", 12)
        pdf.cell(W, LH, line, new_x="LMARGIN", new_y="NEXT")
        pdf.ln(2)
        continue

    # Título de caso de teste  (ex: "1. Teste de Login...")
    if re.match(r"^\d+\.\s+\S", line) and not re.match(r"^\d+\.\s+(Item|Parâmetro|E-mail|Senha|Nome|Novo|Cenário|Nível|Volume|Ano|Status|Termo|Página|Tamanho|Identificador|Possui|Latitude|Longitude|Membros|Cisternas|Data|Consumo)", line):
        if pdf.get_y() > pdf.page_break_trigger - 40:
            pdf.add_page()
        pdf.ln(3)
        pdf.set_font("ARIAL", "B", 12)
        pdf.multi_cell(W, LH, line, align="L")
        pdf.ln(1)
        continue

    # Campo com ● (ex: "● ID do Caso de Teste: CT-001")
    if line.startswith("●"):
        body = line[2:].strip()          # remove "● "
        parts = body.split(":", 1)
        label = parts[0].strip()
        content = parts[1].strip() if len(parts) > 1 else ""

        x0 = 30 + INDENT_FIELD
        w0 = W - INDENT_FIELD

        if label in ("Entradas", "Passos para Execução"):
            # só o rótulo em negrito, conteúdo virá nas linhas seguintes
            pdf.set_x(x0)
            pdf.set_font("ARIAL", "B", 12)
            pdf.cell(w0, LH, "● " + label + ":", new_x="LMARGIN", new_y="NEXT")
        elif label == "Pré-condições" and content == "":
            # pré-condições com sub-itens numerados
            pdf.set_x(x0)
            pdf.set_font("ARIAL", "B", 12)
            pdf.cell(w0, LH, "● " + label + ":", new_x="LMARGIN", new_y="NEXT")
        else:
            pdf.set_x(x0)
            pdf.set_font("ARIAL", "", 12)
            txt = f"**● {label}:** {content}"
            pdf.multi_cell(w0, LH, txt, markdown=True, align="J")
        continue

    # Itens numerados sob Entradas / Passos / Pré-condições
    m = re.match(r"^(\d+)\.\s+(.*)$", line)
    if m:
        x0 = 30 + INDENT_LIST
        w0 = W - INDENT_LIST
        pdf.set_x(x0)
        pdf.set_font("ARIAL", "", 12)
        pdf.multi_cell(w0, LH, line, align="J")
        continue

pdf.output(PDF)
print(f"PDF gerado: {PDF}")
