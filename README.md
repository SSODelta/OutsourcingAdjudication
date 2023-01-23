# Outsourcing Adjudication to Strategic Jurors
This GitHub page is supplementary material to our paper `Outsourcing Adjudication to Strategic Jurors'.

The entry point of the code is Main.java that repeats some of the experiments from the paper and outputs a heatmap.

Requires python3 installed with ``python3`` in PATH to output the final images.

The experiments are as described in the paper. The code itself is also fairly well-documented; for more details, check out the code.

There are also four Python files in the repo that are less documented (and arguably less important):
 * ``minimal_payments.ipynb`` - computes the minimal payment function for n=100 agents. Requires Jupyter installed to run.
 * ``grapher_threshold.py`` - converts ``out.csv`` to a plot for the threshold payment function.
 * ``grapher_awardloss.py`` - converts ``out.csv`` to a plot for the award/loss sharing payment function.
 * ``grapher_list.py`` - converts ``out.csv`` to a plot for the minimal (optimal) payment function from the LP.