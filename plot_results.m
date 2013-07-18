clear
close all
clc


load('./evalresults.txt')

evalResults = evalresults;
clear evalresults

evalCol = 1;
objCol = size(evalResults,2);
parCols = (evalCol+1):(objCol-1);
nPars= numel(parCols);



figure
for iPar=parCols-1
    subplot(nPars,1,iPar)
    plot(evalResults(:,1),evalResults(:,parCols(iPar)),'.k')
end


figure
for iPar=parCols-1
    subplot(nPars,1,iPar)
    hist(evalResults(:,parCols(iPar)),100)
end


r = find(evalResults(:,objCol)==max(evalResults(:,objCol)));


disp(sprintf('%.20f',evalResults(r(1),objCol)))
disp(sprintf('%.20f ',evalResults(r(1),parCols)))


% 74.94062563111670272065
% -1.00005075569713341999 7.00046876468416545691 -5.00125724769418233961 8.99950322149206094480

