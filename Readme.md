---


---

<h2 id="multilevel-parallel-patterns">Multilevel Parallel Patterns</h2>
<p>Mulitlevel Parallel Patterns (MPP for short) is a Java library for creating <em>structured</em> parallel applications that can be executed either on shared memory or distributed memory systems.</p>
<p>The intent to enable parallelism at two levels:</p>
<ul>
<li><strong>Distributed Memory level:</strong> Using the single program, multiple data programming model (SPMD), data can be scattered over a distributed system, with each node (a shared memory system) running an instance of the program. The scattered data is then processed in parallel by the nodes, and the output of each node is combined, giving us the final result of the parallel application. The distribution of data is achieved using message passing via the <em>MPJExpress</em> library. We have created a wrapper class (<code>MPPDistLib</code>) in order to provide intuitive access to common MPJExpress methods and fields.</li>
<li><strong>Shared Memory level:</strong> At the shared memory level, the MPP library aims to provide a <em>top-down</em> approach to structure parallel programs through the use of <em><strong>algorithmic skeletons</strong></em>, which are high-level programming models allowing for abstract descriptions of parallel programs. Each skeleton represents a generic computation pattern: users can simply express the desired parallel computation using these skeletons, the library will take care of the nitty-gritty execution details.<br>
Internally, the execution environment uses <em>Java threads</em> to execute computations as per the skeleton nesting.</li>
</ul>
<p>This library aims to greatly reduce the complexity involved in creating complex parallel applications by allowing users to focus on the <em>structure</em> of the parallel application rather than low-level implementation details.</p>
<p>This is <em><strong>a work in progress</strong></em>, refinements and additions are being made to make the library easier to use.</p>
<h2 id="prerequisites">Prerequisites</h2>
<p><strong>JDK 1.8</strong> or above and the latest version of <strong>maven</strong> are required to use the shared-memory level skeleton library to build parallel applications.</p>
<p>For running applications at the distributed level, the latest version of <strong>MPJExpress</strong> library needs to be installed in all participating nodes. The library and its documentation, including installation instructions, can be found here: <a href="http://mpj-express.org/">http://mpj-express.org/</a></p>
<p><strong>Note:</strong> The <em>MPJExpress</em> library can also be installed on a singular, multi-core, system and be made to run in a <em>Multicore</em> mode, which is useful for building and testing distributed applications when access to a distributed system is limited or unavailable; the application can then be deployed over a distributed system, once available, with minimal effort.</p>
<h2 id="usage">Usage</h2>
<p>The following workflow is typically used to create parallel applications using algorithmic skeletons:</p>
<ul>
<li>Express the parallel structure of the application by using proper skeleton nesting.</li>
<li>Provide application specific sequential portions of the code as skeleton parameters.</li>
<li>Compile/Link the resulting code to obtain the running parallel program</li>
</ul>
<p>For example, in the sample edge detection program provided with the library, there are three sequential operations that are performed in succession on an image. This can be represented by a pipeline skeleton structure:<br>
<a href="https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiZ3JhcGggVEJcblgoKEltYWdlcykpIC0tIFJlYWQgaW1hZ2UgLS0-IEFbU3RhZ2UgMSAtIGJ1aWxkIDNkIG1hdHJpeF1cbkEgLS0gM2QgbWF0cml4IC0tPiBCW1N0YWdlIDIgLSBhcHBseSBmaWx0ZXJdXG5CIC0tIENvbnZvbHZlZCBtYXRyaXgtLT4gQ1tTdGFnZSAzIC0gY3JlYXRlIHJlc3VsdCBpbWFnZV1cbkMgLS0gV3JpdGUgcmVzdWx0IGltYWdlLS0-IE8oKFJlc3VsdCBJbWFnZXMpKVxuIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQiLCJ0aGVtZVZhcmlhYmxlcyI6eyJiYWNrZ3JvdW5kIjoid2hpdGUiLCJwcmltYXJ5Q29sb3IiOiIjRUNFQ0ZGIiwic2Vjb25kYXJ5Q29sb3IiOiIjZmZmZmRlIiwidGVydGlhcnlDb2xvciI6ImhzbCg4MCwgMTAwJSwgOTYuMjc0NTA5ODAzOSUpIiwicHJpbWFyeUJvcmRlckNvbG9yIjoiaHNsKDI0MCwgNjAlLCA4Ni4yNzQ1MDk4MDM5JSkiLCJzZWNvbmRhcnlCb3JkZXJDb2xvciI6ImhzbCg2MCwgNjAlLCA4My41Mjk0MTE3NjQ3JSkiLCJ0ZXJ0aWFyeUJvcmRlckNvbG9yIjoiaHNsKDgwLCA2MCUsIDg2LjI3NDUwOTgwMzklKSIsInByaW1hcnlUZXh0Q29sb3IiOiIjMTMxMzAwIiwic2Vjb25kYXJ5VGV4dENvbG9yIjoiIzAwMDAyMSIsInRlcnRpYXJ5VGV4dENvbG9yIjoicmdiKDkuNTAwMDAwMDAwMSwgOS41MDAwMDAwMDAxLCA5LjUwMDAwMDAwMDEpIiwibGluZUNvbG9yIjoiIzMzMzMzMyIsInRleHRDb2xvciI6IiMzMzMiLCJtYWluQmtnIjoiI0VDRUNGRiIsInNlY29uZEJrZyI6IiNmZmZmZGUiLCJib3JkZXIxIjoiIzkzNzBEQiIsImJvcmRlcjIiOiIjYWFhYTMzIiwiYXJyb3doZWFkQ29sb3IiOiIjMzMzMzMzIiwiZm9udEZhbWlseSI6IlwidHJlYnVjaGV0IG1zXCIsIHZlcmRhbmEsIGFyaWFsIiwiZm9udFNpemUiOiIxNnB4IiwibGFiZWxCYWNrZ3JvdW5kIjoiI2U4ZThlOCIsIm5vZGVCa2ciOiIjRUNFQ0ZGIiwibm9kZUJvcmRlciI6IiM5MzcwREIiLCJjbHVzdGVyQmtnIjoiI2ZmZmZkZSIsImNsdXN0ZXJCb3JkZXIiOiIjYWFhYTMzIiwiZGVmYXVsdExpbmtDb2xvciI6IiMzMzMzMzMiLCJ0aXRsZUNvbG9yIjoiIzMzMyIsImVkZ2VMYWJlbEJhY2tncm91bmQiOiIjZThlOGU4IiwiYWN0b3JCb3JkZXIiOiJoc2woMjU5LjYyNjE2ODIyNDMsIDU5Ljc3NjUzNjMxMjglLCA4Ny45MDE5NjA3ODQzJSkiLCJhY3RvckJrZyI6IiNFQ0VDRkYiLCJhY3RvclRleHRDb2xvciI6ImJsYWNrIiwiYWN0b3JMaW5lQ29sb3IiOiJncmV5Iiwic2lnbmFsQ29sb3IiOiIjMzMzIiwic2lnbmFsVGV4dENvbG9yIjoiIzMzMyIsImxhYmVsQm94QmtnQ29sb3IiOiIjRUNFQ0ZGIiwibGFiZWxCb3hCb3JkZXJDb2xvciI6ImhzbCgyNTkuNjI2MTY4MjI0MywgNTkuNzc2NTM2MzEyOCUsIDg3LjkwMTk2MDc4NDMlKSIsImxhYmVsVGV4dENvbG9yIjoiYmxhY2siLCJsb29wVGV4dENvbG9yIjoiYmxhY2siLCJub3RlQm9yZGVyQ29sb3IiOiIjYWFhYTMzIiwibm90ZUJrZ0NvbG9yIjoiI2ZmZjVhZCIsIm5vdGVUZXh0Q29sb3IiOiJibGFjayIsImFjdGl2YXRpb25Cb3JkZXJDb2xvciI6IiM2NjYiLCJhY3RpdmF0aW9uQmtnQ29sb3IiOiIjZjRmNGY0Iiwic2VxdWVuY2VOdW1iZXJDb2xvciI6IndoaXRlIiwic2VjdGlvbkJrZ0NvbG9yIjoicmdiYSgxMDIsIDEwMiwgMjU1LCAwLjQ5KSIsImFsdFNlY3Rpb25Ca2dDb2xvciI6IndoaXRlIiwic2VjdGlvbkJrZ0NvbG9yMiI6IiNmZmY0MDAiLCJ0YXNrQm9yZGVyQ29sb3IiOiIjNTM0ZmJjIiwidGFza0JrZ0NvbG9yIjoiIzhhOTBkZCIsInRhc2tUZXh0TGlnaHRDb2xvciI6IndoaXRlIiwidGFza1RleHRDb2xvciI6IndoaXRlIiwidGFza1RleHREYXJrQ29sb3IiOiJibGFjayIsInRhc2tUZXh0T3V0c2lkZUNvbG9yIjoiYmxhY2siLCJ0YXNrVGV4dENsaWNrYWJsZUNvbG9yIjoiIzAwMzE2MyIsImFjdGl2ZVRhc2tCb3JkZXJDb2xvciI6IiM1MzRmYmMiLCJhY3RpdmVUYXNrQmtnQ29sb3IiOiIjYmZjN2ZmIiwiZ3JpZENvbG9yIjoibGlnaHRncmV5IiwiZG9uZVRhc2tCa2dDb2xvciI6ImxpZ2h0Z3JleSIsImRvbmVUYXNrQm9yZGVyQ29sb3IiOiJncmV5IiwiY3JpdEJvcmRlckNvbG9yIjoiI2ZmODg4OCIsImNyaXRCa2dDb2xvciI6InJlZCIsInRvZGF5TGluZUNvbG9yIjoicmVkIiwibGFiZWxDb2xvciI6ImJsYWNrIiwiZXJyb3JCa2dDb2xvciI6IiM1NTIyMjIiLCJlcnJvclRleHRDb2xvciI6IiM1NTIyMjIiLCJjbGFzc1RleHQiOiIjMTMxMzAwIiwiZmlsbFR5cGUwIjoiI0VDRUNGRiIsImZpbGxUeXBlMSI6IiNmZmZmZGUiLCJmaWxsVHlwZTIiOiJoc2woMzA0LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTMiOiJoc2woMTI0LCAxMDAlLCA5My41Mjk0MTE3NjQ3JSkiLCJmaWxsVHlwZTQiOiJoc2woMTc2LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTUiOiJoc2woLTQsIDEwMCUsIDkzLjUyOTQxMTc2NDclKSIsImZpbGxUeXBlNiI6ImhzbCg4LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTciOiJoc2woMTg4LCAxMDAlLCA5My41Mjk0MTE3NjQ3JSkifX0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9"><img src="https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggVEJcblgoKEltYWdlcykpIC0tIFJlYWQgaW1hZ2UgLS0-IEFbU3RhZ2UgMSAtIGJ1aWxkIDNkIG1hdHJpeF1cbkEgLS0gM2QgbWF0cml4IC0tPiBCW1N0YWdlIDIgLSBhcHBseSBmaWx0ZXJdXG5CIC0tIENvbnZvbHZlZCBtYXRyaXgtLT4gQ1tTdGFnZSAzIC0gY3JlYXRlIHJlc3VsdCBpbWFnZV1cbkMgLS0gV3JpdGUgcmVzdWx0IGltYWdlLS0-IE8oKFJlc3VsdCBJbWFnZXMpKVxuIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQiLCJ0aGVtZVZhcmlhYmxlcyI6eyJiYWNrZ3JvdW5kIjoid2hpdGUiLCJwcmltYXJ5Q29sb3IiOiIjRUNFQ0ZGIiwic2Vjb25kYXJ5Q29sb3IiOiIjZmZmZmRlIiwidGVydGlhcnlDb2xvciI6ImhzbCg4MCwgMTAwJSwgOTYuMjc0NTA5ODAzOSUpIiwicHJpbWFyeUJvcmRlckNvbG9yIjoiaHNsKDI0MCwgNjAlLCA4Ni4yNzQ1MDk4MDM5JSkiLCJzZWNvbmRhcnlCb3JkZXJDb2xvciI6ImhzbCg2MCwgNjAlLCA4My41Mjk0MTE3NjQ3JSkiLCJ0ZXJ0aWFyeUJvcmRlckNvbG9yIjoiaHNsKDgwLCA2MCUsIDg2LjI3NDUwOTgwMzklKSIsInByaW1hcnlUZXh0Q29sb3IiOiIjMTMxMzAwIiwic2Vjb25kYXJ5VGV4dENvbG9yIjoiIzAwMDAyMSIsInRlcnRpYXJ5VGV4dENvbG9yIjoicmdiKDkuNTAwMDAwMDAwMSwgOS41MDAwMDAwMDAxLCA5LjUwMDAwMDAwMDEpIiwibGluZUNvbG9yIjoiIzMzMzMzMyIsInRleHRDb2xvciI6IiMzMzMiLCJtYWluQmtnIjoiI0VDRUNGRiIsInNlY29uZEJrZyI6IiNmZmZmZGUiLCJib3JkZXIxIjoiIzkzNzBEQiIsImJvcmRlcjIiOiIjYWFhYTMzIiwiYXJyb3doZWFkQ29sb3IiOiIjMzMzMzMzIiwiZm9udEZhbWlseSI6IlwidHJlYnVjaGV0IG1zXCIsIHZlcmRhbmEsIGFyaWFsIiwiZm9udFNpemUiOiIxNnB4IiwibGFiZWxCYWNrZ3JvdW5kIjoiI2U4ZThlOCIsIm5vZGVCa2ciOiIjRUNFQ0ZGIiwibm9kZUJvcmRlciI6IiM5MzcwREIiLCJjbHVzdGVyQmtnIjoiI2ZmZmZkZSIsImNsdXN0ZXJCb3JkZXIiOiIjYWFhYTMzIiwiZGVmYXVsdExpbmtDb2xvciI6IiMzMzMzMzMiLCJ0aXRsZUNvbG9yIjoiIzMzMyIsImVkZ2VMYWJlbEJhY2tncm91bmQiOiIjZThlOGU4IiwiYWN0b3JCb3JkZXIiOiJoc2woMjU5LjYyNjE2ODIyNDMsIDU5Ljc3NjUzNjMxMjglLCA4Ny45MDE5NjA3ODQzJSkiLCJhY3RvckJrZyI6IiNFQ0VDRkYiLCJhY3RvclRleHRDb2xvciI6ImJsYWNrIiwiYWN0b3JMaW5lQ29sb3IiOiJncmV5Iiwic2lnbmFsQ29sb3IiOiIjMzMzIiwic2lnbmFsVGV4dENvbG9yIjoiIzMzMyIsImxhYmVsQm94QmtnQ29sb3IiOiIjRUNFQ0ZGIiwibGFiZWxCb3hCb3JkZXJDb2xvciI6ImhzbCgyNTkuNjI2MTY4MjI0MywgNTkuNzc2NTM2MzEyOCUsIDg3LjkwMTk2MDc4NDMlKSIsImxhYmVsVGV4dENvbG9yIjoiYmxhY2siLCJsb29wVGV4dENvbG9yIjoiYmxhY2siLCJub3RlQm9yZGVyQ29sb3IiOiIjYWFhYTMzIiwibm90ZUJrZ0NvbG9yIjoiI2ZmZjVhZCIsIm5vdGVUZXh0Q29sb3IiOiJibGFjayIsImFjdGl2YXRpb25Cb3JkZXJDb2xvciI6IiM2NjYiLCJhY3RpdmF0aW9uQmtnQ29sb3IiOiIjZjRmNGY0Iiwic2VxdWVuY2VOdW1iZXJDb2xvciI6IndoaXRlIiwic2VjdGlvbkJrZ0NvbG9yIjoicmdiYSgxMDIsIDEwMiwgMjU1LCAwLjQ5KSIsImFsdFNlY3Rpb25Ca2dDb2xvciI6IndoaXRlIiwic2VjdGlvbkJrZ0NvbG9yMiI6IiNmZmY0MDAiLCJ0YXNrQm9yZGVyQ29sb3IiOiIjNTM0ZmJjIiwidGFza0JrZ0NvbG9yIjoiIzhhOTBkZCIsInRhc2tUZXh0TGlnaHRDb2xvciI6IndoaXRlIiwidGFza1RleHRDb2xvciI6IndoaXRlIiwidGFza1RleHREYXJrQ29sb3IiOiJibGFjayIsInRhc2tUZXh0T3V0c2lkZUNvbG9yIjoiYmxhY2siLCJ0YXNrVGV4dENsaWNrYWJsZUNvbG9yIjoiIzAwMzE2MyIsImFjdGl2ZVRhc2tCb3JkZXJDb2xvciI6IiM1MzRmYmMiLCJhY3RpdmVUYXNrQmtnQ29sb3IiOiIjYmZjN2ZmIiwiZ3JpZENvbG9yIjoibGlnaHRncmV5IiwiZG9uZVRhc2tCa2dDb2xvciI6ImxpZ2h0Z3JleSIsImRvbmVUYXNrQm9yZGVyQ29sb3IiOiJncmV5IiwiY3JpdEJvcmRlckNvbG9yIjoiI2ZmODg4OCIsImNyaXRCa2dDb2xvciI6InJlZCIsInRvZGF5TGluZUNvbG9yIjoicmVkIiwibGFiZWxDb2xvciI6ImJsYWNrIiwiZXJyb3JCa2dDb2xvciI6IiM1NTIyMjIiLCJlcnJvclRleHRDb2xvciI6IiM1NTIyMjIiLCJjbGFzc1RleHQiOiIjMTMxMzAwIiwiZmlsbFR5cGUwIjoiI0VDRUNGRiIsImZpbGxUeXBlMSI6IiNmZmZmZGUiLCJmaWxsVHlwZTIiOiJoc2woMzA0LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTMiOiJoc2woMTI0LCAxMDAlLCA5My41Mjk0MTE3NjQ3JSkiLCJmaWxsVHlwZTQiOiJoc2woMTc2LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTUiOiJoc2woLTQsIDEwMCUsIDkzLjUyOTQxMTc2NDclKSIsImZpbGxUeXBlNiI6ImhzbCg4LCAxMDAlLCA5Ni4yNzQ1MDk4MDM5JSkiLCJmaWxsVHlwZTciOiJoc2woMTg4LCAxMDAlLCA5My41Mjk0MTE3NjQ3JSkifX0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9" alt=""></a><br>
Each stage in the pipeline can be performed in parallel for a given set of images. We provide an interface called `Operation’ that needs to be implemented to provide sequential portions of the code that are intended to be parallelized.</p>
<p>Following is a snippet for stage 1 of the edge detection program:</p>
<pre><code>public class ImageConvolutionStage1 implements Operation&lt;BufferedImage, double[][][]&gt; { 

	@Override
	public double[][][] execute(BufferedImage inputParam) throws Exception {		
		// This method contains the sequential code that is intended to run in parallel with other sequential code
		int width = inputParam.getWidth();
		int height = inputParam.getHeight();
		...	
</code></pre>
<p>Once we have out sequential portions of code, we need to nest it in the <code>SequentialOpSkeleton</code> which is one of the three <code>Skeleton</code> implementations currently supported by the library.</p>
<pre><code>Skeleton stage1 = new SequentialOpSkeleton&lt;BufferedImage, double[][][]&gt;(o1, double[][][].class);
Skeleton stage2 = new SequentialOpSkeleton&lt;double[][][], double[][]&gt;(o2, double[][].class);
...
</code></pre>
<p><br><br>
Now that we have three sequential skeletons ready, we can create our final <code>PipelineSkeleton</code> which represents the outermost skeleton nesting to which we supply the input (list of images)</p>
<pre><code>Skeleton[] stages = { stage1, stage2, stage3 };
Skeleton pipeLine = new PipelineSkeleton(stages, ArrayList.class); // The outermost skeleton nesting
Future&lt;List&lt;File&gt;&gt; outputFuture = pipeLine.submitData(imageList); // Submit input data to the pipeline skeleton
</code></pre>
<p><br> The result of the parallel computation is represented by a <code>Future</code> object.</p>
<pre><code>Future&lt;List&lt;File&gt;&gt; outputFuture = pipeLine.submitData(imageList); // Future object containing the result of the parallel computation
</code></pre>
<h2 id="examples">Examples</h2>
<h3 id="synthetic-benchmarks">Synthetic Benchmarks</h3>
<p>A few synthetic benchmarks for the shared memory skeletons are included. These can executed by the following:<br>
Navigate to the root of the project folder (one containing the pom.xml file)<br>
Compile source code:</p>
<pre><code>mvn compile
</code></pre>
<p>The different benchmarks can be run via the following commands:</p>
<ul>
<li>
<p><u>Three stage pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ThreeStagePipeline [numberOfCores][inputSize]
</code></pre>
</li>
<li>
<p><u>Task farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[chunkSize]</em> with the number of tasks per worker<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.SimpleTaskFarm [numberOfCores][inputSize][chunkSize][numberOfWorkers]
</code></pre>
</li>
<li>
<p><u>Nesting pipeline in farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.NestingPipelineInFarm [numberOfCores][inputSize][numberOfWorkers]
</code></pre>
</li>
<li>
<p><u>Nesting farm in pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the benchmark with<br>
<em>[inputSize]</em> with the number of tasks to be executed<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm</p>
<pre><code> java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.NestingFarmInPipeline [numberOfCores][inputSize][numberOfWorkers]
</code></pre>
</li>
</ul>
<h3 id="edge-detection">Edge Detection</h3>
<p>A simple edge detection program is also included in the library as an example. Images can be placed in the<br>
<code>examples/src/main/resources/inputImages/test</code> folder. After executing the program, the resultant images will be stored in the <code>examples/src/main/resources/outputImages</code> folder.</p>
<p>The following commands can be used to run the program in different skeleton configurations:</p>
<ul>
<li><u>Pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program with<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImageConvolutionPipeline [numberOfCores] 3
</code></pre>
</li>
<li><u>Farm in pipeline:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program with<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" 	uk.ac.standrews.cs.mpp.examples.skeletons.ImgConvolNestingFarmInPipeline [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
<li><u>Pipeline in farm:</u><br>
replace:<br>
<em>[numberOfCores]</em> with the number of processors to run the program, with<br>
<em>[ numberOfWorkers]</em> with the total number of workers in the farm<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImgConvolNestingPipelineInFarm [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
</ul>
<p>The performance of these parallel versions can be measured up against a sequential version of the same program which can be run by the following command</p>
<pre><code>java -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.skeletons.ImageConvolutionSequential 3
</code></pre>
<h4 id="running-the-distributed-version">Running the distributed version</h4>
<p><em>MPJExpress</em> needs to be installed and configured in order to run the distributed version of the synthetic benchmark and edge detection application.  They can be run in the multi-core mode of <em>MPJExpress</em> through the following commands</p>
<ul>
<li>
<p><u>Edge Detection \w Pipeline Skeleton</u><br>
replace:<br>
<em>[numberOfSystems]</em> with the number of nodes in the distributed system (in the multi-core mode of <em>MPJExpress</em>, this refers to the number of cores)<br>
<em>[numberOfCores]</em> with the number of processors at the shared memory level</p>
<pre><code>mpjrun -np [numberOfSystems] -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.mpj.MPPDistImgConvolution [numberOfCores] 3
</code></pre>
</li>
<li>
<p><u>Edge Detection \w Nested Skeletons</u><br>
replace:<br>
<em>[numberOfSystems]</em> with the number of nodes in the 		distributed system (in the multi-core mode of <em>MPJExpress</em>, this refers to the number of cores)<br>
<em>[numberOfCores]</em> with the number of processors at the shared memory level	<br>
[numberOfWorkers] with the total number of workers in the farm</p>
<pre><code>mpjrun -np [numberOfSystems] -cp ".\lib\mpj-0.44.jar;.\target\classes" uk.ac.standrews.cs.mpp.examples.mpj.MPPDistImgConvolutionPipeInFarm [numberOfCores] 3 [numberOfWorkers]
</code></pre>
</li>
</ul>
<h3 id="sample-outputs">Sample Outputs</h3>
<p>Following is a sample output of the image convolution application:</p>
<p><em><strong>WIP - Sample outputs to be added soon!</strong></em></p>
<h2 id="performance">Performance</h2>
<p>Following are performance figures of the benchmarks and the edge detection application when executed on a research server having two Intel® Xeon® CPUs clocked at 2.60 Ghz with 14 cores each, for a total of 28 cores.</p>
<h3 id="edge-detection-program">Edge detection program</h3>
<p>The maximum speedup for the distributed edge detection program for a total of 488 images (resolutions ranging between 1080p and 600p) is <em><strong>6.7</strong></em>.<br>
<strong>The sequential version runs in 97</strong> seconds while the parallel version, using the <em>Pipe(Seq, Farm(Seq), Seq)</em> skeleton<br>
composition with 8 nodes and 4 farm workers per node, <strong>runs in 19.3 seconds.</strong></p>
<p><em><strong>WIP - Supporting graphs and figures to be added soon!</strong></em></p>
<blockquote>
<p>Written with <a href="https://stackedit.io/">StackEdit</a>.</p>
</blockquote>

