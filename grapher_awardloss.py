import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns; sns.set_theme()
from  matplotlib.colors import LinearSegmentedColormap
from datetime import datetime

X = np.loadtxt(open("out.csv", "rb"), delimiter=",")


# Format numbers for the heatmap
# x: a number, possibly an int, possibly a float
def rnd(x):
    # if float is an integer
    if round(x) == x or x > 15:
        return int(x)

    # else, use one digit
    return np.round(x,1)

# Setup color map (boilerplate)
def cmap():
    c = ["darkred","red","lightcoral","white", "palegreen","green","darkgreen"]
    v = [0,.15,.4,.5,0.6,.9,1.]
    l = list(zip(v,c))
    return LinearSegmentedColormap.from_list('rg',l, N=256)

# Plot heatmap
cmap = sns.color_palette("viridis", as_cmap=True)
ax=sns.heatmap(X, vmin=0, vmax=1, cmap=cmap)
ax.set_xlabel("Size of reward (ω)",fontsize=25)
ax.set_ylabel("Percentage of well-informed jurors (ρ)",fontsize=25)
ax.set_title('Correctness of Adjudication',fontsize=30)

resy, resx = X.shape
#xrange = [50,100]
#xrange = [0,5]
xrange = [0,100]

yrange = [1,0]
xs = np.linspace(xrange[0], xrange[1], num=resx+1)
ys = np.linspace(yrange[0], yrange[1], num=resy+1)

ax.set_xticks([resx/5*i+0.5 for i in range(5)] + [resx])
ax.set_yticks([resy/5*i+0.5 for i in range(5)] + [resy])

ax.set_xticklabels([rnd(xs[int(resx/5*i)]) for i in range(5)] + [rnd(xrange[1])], fontsize=16)
ax.set_yticklabels([rnd(ys[int(resy/5*i)]) for i in range(5)] + [rnd(yrange[1])], fontsize=16)
plt.subplots_adjust(bottom=0.15)
ax.figure.set_size_inches(14, 10)


dt = datetime.now()

ax.figure.savefig("imgs/"+dt.strftime("%Y-%m-%d %H_%M_%S") + ".png")
np.savetxt("data/"+dt.strftime("%Y-%m-%d %H_%M_%S") + ".csv", X, delimiter=",")