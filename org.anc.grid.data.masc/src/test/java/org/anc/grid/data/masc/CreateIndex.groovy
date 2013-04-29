package org.anc.grid.data.masc

class CreateIndex
{
   File masc = new File('/var/corpora/MASC-3.0.0')
   File header = new File(masc, 'resource-header.xml')
   File data = new File(masc, 'data')
   
   PrintWriter out;
   
   public CreateIndex()
   {
      
   }

   void run()
   {
      out = new PrintWriter('src/main/resources/masc3.index')
      List types = getTypes(header)
      types << 'txt'
      create(types)
      out.flush()
      out.close()
      println "Done."
   }
   
   void create(List types)
   {
      XmlParser parser = new XmlParser()
      data.eachDirRecurse { dir ->
         dir.eachFileMatch(~/.*\.hdr/) { file ->
            def header = parser.parse(file)
            String id = header.@docId
            out.println "${id}-hdr ${file.path}"
            types.each { type ->
               emit(id, file, type)
            }
         }
      }
   }
   
   List getTypes(File headerFile)
   {
      def xml = new groovy.xml.Namespace('http://www.w3.org/XML/1998/namespace')
      List types = []
      XmlParser parser = new XmlParser()
      def header = parser.parse(headerFile)
      header.resourceDesc.annotationDecls.annotationDecl.each { decl ->
         types << decl.attribute(xml.id).replace('a.', '')
      }
      return types
   }
   
   void emit(String id, File file, String type) 
   {
      String suffix
      if (type == 'txt')
      {
         suffix = ".txt"
      }
      else
      {
         suffix = "-${type}.xml"
      }
      String name = file.name.replace(".hdr", suffix)
      File newFile = new File(file.parentFile, name)
      if (newFile.exists())
      {
         out.println "${id}-${type} ${newFile.path}"
      }
   }
   
   static void main(args)
   {
      new CreateIndex().run()
   }
}
