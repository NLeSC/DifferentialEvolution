Java implementation of the Differential Evolution algorithm by Storn & Price.

Additionally uses Metropolis algorithm to estimate the parameter uncertainty.

The software includes some simple
visualizations using JFreeChart (Java) as well as some simple D3.js (JavaScript).

Standard plotting routines include:

1. marginal parameter histograms;
2. matrix of 2-D parameter correlations (scatter);
3. matrix of 2-D parameter correlations (heatmap);
4. parameter evolution scatter;
5. objective score evolution.

Currently, the Differential Evolution algorithm can be used to optimize any one of 6 models:



| index | Java model class name | number of parameters | description    |       
| ----- | --------------------- | -------------------- | ---------------|
| 1     | CubicModel            | 4                    | polynomial |
| 2     | DoubleNormalModel     | 4                    | two Gaussians, benchmark check on the accuracy of the Metropolis part |
| 3     | LinearDynamicModel    | 1                    | draining linear tank |
| 4     | RastriginModel        | 2                    | benchmark model with response surface containing many local minima |
| 5     | RosenbrockModel        | 2                    | benchmark model with response surface containing many local minima, large insensitive areas, and curved ridges |
| 6     | SingleNormalModel | 2                    | simpler version of the DoubleNormalModel |
