
####################################
## libraries
####################################

library(getopt)
#library(RMySQL)
library(psy)


####################################
## global vars
####################################

prgName<-"ComputeResult.R"
#dirToStore<-"/tmp/"
#dirToStore<-"E:/OSCE Manager/LEARN_R/OsceData/"
#fileImageToStore   <-paste(dirToStore,"output-graphics.png",sep="")
#fileDataToStore    <-paste(dirToStore,"output-data.csv",sep="")
#fileMetricsToStore <-paste(dirToStore,"output-metrics.csv",sep="")

.checkArg <- function(){
 
   opt<-getopt(matrix(c(
                       "filename","f",".","character",
                       "missing", "m",".","list",
                       "range",   "r",".","character",
                       "clean",   "c",  0,"logical",
                       "help",    "h",  0,"logical"
                      ),
                     ncol=4,byrow=TRUE
                    )
             )
   return(c(opt$filename,opt$range,opt$missing))
}


.validateArg <- function(x){

  filename<-x[1]
  range   <-x[2]
  missing <-x[3]
 
  if(grepl("^[[:digit:]]{1,2}[.]{2}[[:digit:]]{1,2}$",range,perl=TRUE)){
    y<-as.integer(unlist(strsplit(range,"\\.\\.")[1]))
    rangeLeft <-y[1]
    rangeRight<-y[2]

    if(rangeLeft>=rangeRight){
      .errorMessage(paste("invalid range '",range,"'",sep=""))
    }
  }
  
  y<-paste("grep -c '|'",filename)

  if(y=="0"){
    .errorMessage(paste("can't find the proper field delimiter in '",filename,"'",sep=""))
  }

  return(c(filename,rangeLeft,rangeRight,missing))

}


####################################
## errorMessage
####################################

.errorMessage <- function(message){

  # STDOUT
  cat("\n")
  cat(paste("  Error executing ",prgName,":\n",sep=""))
  cat("   ",message,"\n")

  # exit
  cat("\n")
  cat("  ...exit\n")
  cat("\n")
  q()

}

####################################
## checkData
####################################

.checkData <- function(x){

  filename  <-x[1]
  rangeLeft <-as.integer(x[2])
  rangeRight<-as.integer(x[3])

  if(!all(complete.cases(df))){
    .errorMessage(paste("the dataset '",filename,"' contains missing value(s)",sep=""))
  }

  if(!all(sapply(df[,3:nCol],is.numeric))){
    .errorMessage(paste("non-numeric values for some items in '",filename,"'",sep=""))
  }

  if(!all(df[,nCol]>=rangeLeft) || !all(df[,nCol]<=rangeRight)){
    .errorMessage("some impression's codes are not in range")
  }

  if(!all(df[,nCol]%%1==0)){
    .errorMessage("some impression's codes are not whole numbers")
  }

}


####################################
## visualize
####################################

.visualize <- function(fname){

  #mainTitle<-paste("Data Set: '",fname,"'",sep="")
  xlab<-"impression's code"
  ylab<-"achieved score (% of maximum)"
  passMarkValue<-paste("pass mark =",round(passMark,digits=2),"%")

  xlim<-c(rangeLeft,rangeRight)
  ylim<-c(0,100)

  xTicks<-seq(rangeLeft,rangeRight,1)
  yTicks<-seq(0,100,10)

  colMain    <-"#204a87"
  colAxis    <-"#2e3436"
  colGreed   <-"#babdb6"
  colPoints  <-"#5c3566"
  colPassMark<-"#f57900"
  colPass    <-"#4e9a06"
  colFall    <-"#cc0000"

  fontSize<-1
  cexAxis<-fontSize*0.7
  cexPassMarkValue<-fontSize*1.1
  cexPassPoints<-fontSize*0.4

  taktTime<-0

  # opening graphic device
  png(fname)

  # plotting region
  plot(NULL,xlim=xlim,ylim=ylim,axes=FALSE,main="",xlab=xlab,ylab=ylab,
       col.main=colMain,col.lab=colMain)
  axis(1,xTicks,col=colAxis,cex.axis=cexAxis,col.axis=colAxis)
  axis(2,yTicks,col=colAxis,cex.axis=cexAxis,col.axis=colAxis,las=1)
  grid(nx=0,ny=20,lty="dotted",col=colGreed)
  Sys.sleep(taktTime)

  # plotting input data
  points(x,y,pch=21,col=colPoints)
  Sys.sleep(taktTime)

  # plotting regression line
  abline(z,col=colPoints)
  Sys.sleep(taktTime)

  # plotting pass mark line
  abline(h=passMark,lty=5,col=colPassMark)
  Sys.sleep(taktTime)

  # adding value pass mark
  mtext(passMarkValue,side=1,line=-4,cex=cexPassMarkValue,col=colPassMark) 
  Sys.sleep(taktTime)

  # coloring pass points
  points(xPass,yPass,pch=11,cex=cexPassPoints,col=colPass)
  Sys.sleep(taktTime)

  # coloring fall points
  points(xFall,yFall,pch=11,cex=cexPassPoints,col=colFall)
  Sys.sleep(taktTime)

}

####################################
## endOfProgram
####################################

.endOfProgram <- function(){

  cat("\n")
  cat("  ...done\n")
  cat("\n")
  q()

}

####################################
## Main
####################################

.checkArg()->x

.validateArg(x)->x

filename  <-x[1]
rangeLeft <-as.integer(x[2])
rangeRight<-as.integer(x[3])
missing   <- strsplit(x[4], ",")

fname <- sub(".csv", ".png", filename)

df<-read.table(filename,header=T,sep="|",na.strings=c("NA","na","NULL","#NULL!"),dec=".")

nRow<-nrow(df)
nCol<-ncol(df)

cat("\n")
cat("  analysing data set: a table of",nRow,"rows and",nCol,"columns\n")

.checkData(x)

cat("\n")
cat(paste("  the range for impression's code: [",rangeLeft,";",rangeRight,"]\n",sep=""))

rangeAverage<-round(rangeLeft+(rangeRight-rangeLeft)/2,1)

cat("\n")
cat("  computing pass mark based on the average impression's code",rangeAverage,"\n")

testColName <- c(missing[[1]])

cat("testColName : ", testColName, "\n")
df1 <- df[3:(nCol-1)]

if (0 %in% testColName){
	y<-rowSums(df[3:(nCol-1)])
} else{

	testCol<-which(names(df) %in% testColName)
	
	if (length(testCol) != 0)
	{	
		y<-rowSums(df1[-testCol])
	}
	else
	{
		y<-rowSums(df[3:(nCol-1)])
	}
}

# computing linear model (linear regression)
x<-df[,nCol]
z<-lm(y~x)
beta0<-z$coefficients[1]
beta1<-z$coefficients[2]

# computing pass mark, pass points, and fall points
passMark<-2*beta1+beta0
xPass<-x[y>=passMark]
yPass<-y[y>=passMark]
xFall<-x[y< passMark]
yFall<-y[y< passMark]

cat("\n")
cat(paste("  generating graphics (s. file '",fname,"')\n",sep=""))

.visualize(fname)

.endOfProgram()